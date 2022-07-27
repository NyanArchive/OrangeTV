package tv.orange.features.chat

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.core.models.Flag.Companion.variant
import tv.orange.core.models.FlagListener
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.core.models.variants.DeletedMessages
import tv.orange.features.badges.bridge.OrangeMessageBadge
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.chat.bridge.*
import tv.orange.features.chat.di.component.DaggerChatComponent
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.models.abs.EmoteCardModelWrapper
import tv.orange.models.abs.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.models.emotes.EmoteCardModelResponse
import tv.twitch.android.models.emotes.EmoteSet
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.chat.adapter.item.ChatMessageClickedEvents
import tv.twitch.android.shared.chat.util.ChatUtil
import tv.twitch.android.shared.chat.util.ClickableUsernameSpan
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteHeaderUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmotePickerSection
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiSet
import tv.twitch.android.shared.emotes.models.EmoteMessageInput
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan
import tv.twitch.android.shared.ui.elements.span.UrlDrawable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatHookProvider @Inject constructor(
    val emoteProvider: EmoteProvider,
    val badgeProvider: BadgeProvider
) : LifecycleAware, FlagListener {
    private val currentChannelSubject = BehaviorSubject.create<Int>()

    fun registerLifecycle(
        controller: LifecycleController,
        preferenceManager: PreferenceManager
    ) {
        controller.registerLifecycleListeners(this)
        preferenceManager.registerFlagListeners(this)
    }

    fun hookMessageInterface(
        cmi: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        val badges = injectBadges(cmi.badges, cmi.userId, channelId)
        val tokens = injectEmotes(cmi.tokens, cmi.userId, channelId)

        return ChatMessageInterfaceWrapper(cmi = cmi, badges = badges, tokens = tokens)
    }

    private fun injectBadges(
        badges: List<MessageBadge>,
        userId: Int,
        channelId: Int
    ): List<MessageBadge> {
        if (!badgeProvider.hasBadges(userId)) {
            return badges
        }

        val newBadges = badgeProvider.getBadges(userId).toMutableList()
        if (newBadges.isEmpty()) {
            return badges
        }

        val stack = mutableListOf<MessageBadge>()
        badges.forEach { badge ->
            val replaces = newBadges.firstOrNull { it.getReplaces() == badge.name }

            if (replaces != null) {
                stack.add(
                    OrangeMessageBadge(
                        replaces.getCode(),
                        replaces.getUrl(),
                        replaces.getBackgroundColor()
                    )
                )
                newBadges.remove(replaces)
            } else {
                stack.add(badge)
            }
        }
        stack.addAll(newBadges.map {
            OrangeMessageBadge(it.getCode(), it.getUrl(), it.getBackgroundColor())
        })

        return stack
    }

    private fun injectEmotes(
        tokens: List<MessageToken>,
        userId: Int,
        channelId: Int
    ): List<MessageToken> {
        val stack = mutableListOf<MessageToken>()

        var injected = false
        tokens.forEach { token ->
            if (token is MessageToken.TextToken) {
                val words = token.text.split(" ")
                for (word in words) {
                    val emote = emoteProvider.getEmote(word, channelId)
                    if (emote != null) {
                        if (!injected) {
                            injected = true
                        }
                        stack.add(
                            EmoteToken(
                                emote.getCode(),
                                emote.getUrl(Emote.Size.MEDIUM),
                                emote.getUrl(Emote.Size.LARGE),
                                emote.getPackageSet()
                            )
                        )
                    } else {
                        stack.add(MessageToken.TextToken("$word ", token.flags))
                    }
                }
            } else {
                stack.add(token)
            }
        }

        if (injected) {
            return stack
        }

        return tokens
    }

    override fun onAllComponentDestroyed() {
        emoteProvider.clear()
        badgeProvider.clear()
    }

    override fun onSdkResume() {
        badgeProvider.refreshBadges()
        emoteProvider.fetch()
    }

    override fun onFirstActivityCreated() {
        badgeProvider.fetchBadges()
        emoteProvider.fetch()
    }

    override fun onConnectingToChannel(channelId: Int) {
        emoteProvider.requestChannelEmotes(channelId)
        currentChannelSubject.onNext(channelId)
    }

    override fun onFlagChanged(flag: Flag) {
        when (flag) {
            Flag.BTTV_EMOTES, Flag.FFZ_EMOTES, Flag.STV_EMOTES -> {
                emoteProvider.rebuild()
            }
            Flag.FFZ_BADGES, Flag.STV_BADGES, Flag.CHA_BADGES, Flag.CHE_BADGES -> {
                badgeProvider.rebuild()
            }
            else -> {}
        }
    }

    override fun onAllComponentStopped() {}
    override fun onAccountLogout() {}
    override fun onFirstActivityStarted() {}
    override fun onConnectedToChannel(channelId: Int) {}

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun hookEmoteSetsFlowable(
        map: Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>>,
        num: Integer
    ): Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>> {
        return map.map { pair ->
            emoteProvider.getEmotesMap(num.toInt()).filter { it.second.isNotEmpty() }
                .forEach { emotePair ->
                    pair.second.add(
                        EmoteUiSet(
                            EmoteHeaderUiModel.EmoteHeaderStringResUiModel(
                                packageTokenToId(emotePair.first),
                                true,
                                EmotePickerSection.ALL,
                                false
                            ), emotePair.second.map { emote ->
                                createEmoteUiModel(
                                    emote = emote,
                                    channelId = num.toInt(),
                                    isAnimated = false
                                )
                            })
                    )
                }

            return@map pair
        }
    }

    fun hookEmoteCardModelResponse(emoteId: String?): EmoteCardModelResponse? {
        if (emoteId.isNullOrBlank()) {
            return null
        }

        val model = EmoteCardModelWrapper.fromString(emoteId) ?: return null

        return EmoteCardModelResponse.Success(
            OrangeEmoteCardModel(
                token = model.token,
                url = model.url,
                set = model.set
            )
        )
    }

    fun hookAutoCompleteMapProvider(emotesFlow: Flowable<List<EmoteSet>>): Flowable<List<EmoteSet>> {
        return emotesFlow.flatMap { orgList ->
            currentChannelSubject.flatMap {
                Observable.just(it).delay(3, TimeUnit.SECONDS)
            }.toFlowable(BackpressureStrategy.LATEST).flatMap { channelId ->
                val newSets = emoteProvider.getEmotesMap(channelId = channelId).map { pair ->
                    OrangeEmoteSet(pair.second.map {
                        OrangeEmoteModel(
                            emoteToken = it.getCode(),
                            emoteUrl = it.getUrl(Emote.Size.MEDIUM)
                        )
                    })
                }
                Flowable.just((orgList + newSets)).doOnNext { Logger.debug("Injected: $it") }
            }
        }
    }

    fun hookMarkAsDeleted(
        messageId: String?,
        message: Spanned?,
        context: Context?,
        eventDispatcher: PublishSubject<ChatMessageClickedEvents?>?,
        hasModAccess: Boolean
    ): Spanned? {
        return when (Flag.DELETED_MESSAGES.variant<DeletedMessages>()) {
            DeletedMessages.Mod -> ChatUtil.Companion!!.createDeletedSpanFromChatMessageSpan(
                messageId,
                message,
                context,
                eventDispatcher,
                true
            )
            DeletedMessages.Strikethrough -> createDeletedStrikethrough(message)
            DeletedMessages.Grey -> createDeletedGrey(message)
            DeletedMessages.Default -> ChatUtil.Companion!!.createDeletedSpanFromChatMessageSpan(
                messageId,
                message,
                context,
                eventDispatcher,
                hasModAccess
            )
        }
    }

    fun rebuildEmotes() {
        emoteProvider.rebuild()
    }

    companion object {
        private val INSTANCE: ChatHookProvider by lazy {
            val hook = DaggerChatComponent.builder()
                .badgesComponent(Core.getProvider(BadgesComponent::class).get())
                .emotesComponent(Core.getProvider(EmotesComponent::class).get())
                .build().hook

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        private fun packageTokenToId(token: EmotePackageSet): Int {
            val resName = when (token) {
                EmotePackageSet.BttvGlobal -> "orange_bttv_global_emotes"
                EmotePackageSet.BttvChannel -> "orange_bttv_channel_emotes"
                EmotePackageSet.FfzGlobal -> "orange_ffz_global_emotes"
                EmotePackageSet.FfzChannel -> "orange_ffz_channel_emotes"
                EmotePackageSet.StvGlobal -> "orange_stv_global_emotes"
                EmotePackageSet.StvChannel -> "orange_stv_channel_emotes"
                else -> "orange_unknown_emotes"
            }

            return ResourceManager.getId(resName = resName, "string")
        }

        @JvmStatic
        fun get(): ChatHookProvider {
            return INSTANCE
        }

        @JvmStatic
        fun enableStickyHeaders(): Boolean {
            return !Flag.DISABLE_STICKY_HEADERS_EP.valueBoolean()
        }

        @JvmStatic
        fun changeBitsButtonVisibility(org: Boolean): Boolean {
            if (Flag.HIDE_BITS_BUTTON.valueBoolean()) {
                return false
            }

            return org
        }

        private fun createEmoteUiModel(
            emote: Emote,
            channelId: Int,
            isAnimated: Boolean
        ): EmoteUiModel {
            val emoteMessageInput = EmoteMessageInput(emote.getCode(), "-1", false)
            val emotePicker = EmotePickerEmoteModelExt("-1", emote.getCode(), channelId, isAnimated)
            val clickedEmote = EmotePickerPresenter.ClickedEmote.Unlocked(
                emotePicker,
                emoteMessageInput,
                null,
                emptyList()
            )
            return EmoteUiModelExt.EmoteUiModelWithUrl(
                "-1", clickedEmote,
                emote.getUrl(Emote.Size.LARGE), isAnimated
            )
        }

        @JvmStatic
        fun hook(
            messageId: String?,
            message: Spanned?,
            context: Context?,
            messageClickEventDispatcher: PublishSubject<ChatMessageClickedEvents?>?,
            hasModAccess: Boolean
        ): Spanned? {
            return get().hookMarkAsDeleted(
                messageId,
                message,
                context,
                messageClickEventDispatcher,
                hasModAccess
            )
        }

        private fun createDeletedGrey(msg: Spanned?): Spanned? {
            if (msg.isNullOrBlank()) {
                return msg
            }

            msg.getSpans(0, msg.length, ForegroundColorSpan::class.java)?.forEach {
                if (it.foregroundColor == Color.GRAY) {
                    return msg
                }
            }

            val message = SpannableStringBuilder(msg)
            message.getSpans(0, msg.length, ForegroundColorSpan::class.java)?.forEach {
                message.removeSpan(it)
            }

            message.getSpans(0, msg.length, CenteredImageSpan::class.java)?.forEach {
                val drawable = it.imageDrawable
                if (drawable is UrlDrawable) {
                    drawable.setGrey(true)
                }
            }
            message.setSpan(
                ForegroundColorSpan(Color.GRAY),
                0,
                message.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return SpannedString.valueOf(message)
        }

        private fun createDeletedStrikethrough(msg: Spanned?): Spanned? {
            if (msg.isNullOrBlank()) {
                return msg
            }

            msg.getSpans(0, msg.length, StrikethroughSpan::class.java)?.let { spans ->
                if (spans.isNotEmpty()) {
                    return msg
                }
            }

            val message = SpannableStringBuilder(msg).apply {
                setSpan(
                    StrikethroughSpan(),
                    getMessageStartPos(msg),
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return SpannedString.valueOf(message)
        }

        private fun getMessageStartPos(msg: Spanned): Int {
            var usernameEndPos = 0
            val spans = msg.getSpans(0, msg.length, ClickableUsernameSpan::class.java)
            if (spans.isEmpty()) {
                return usernameEndPos
            }
            usernameEndPos = msg.getSpanEnd(spans[0])
            val pos = usernameEndPos + 2
            if (pos < msg.length) {
                if (TextUtils.equals(msg.subSequence(usernameEndPos, pos), ": ")) {
                    usernameEndPos = pos
                }
            }
            return usernameEndPos
        }
    }
}

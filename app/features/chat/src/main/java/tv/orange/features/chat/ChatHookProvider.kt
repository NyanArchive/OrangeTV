package tv.orange.features.chat

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.widget.ImageView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.core.models.flag.variants.DeletedMessages
import tv.orange.features.badges.bridge.OrangeMessageBadge
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.chat.bridge.*
import tv.orange.features.chat.view.ViewFactory
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.models.abc.EmoteCardModelWrapper
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.abc.Feature
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
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerViewDelegate
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteHeaderUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmotePickerSection
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiSet
import tv.twitch.android.shared.emotes.models.EmoteMessageInput
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan
import tv.twitch.android.shared.ui.elements.span.UrlDrawable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatHookProvider @Inject constructor(
    val emoteProvider: EmoteProvider,
    val badgeProvider: BadgeProvider,
    val viewFactory: ViewFactory
) : LifecycleAware, FlagListener, Feature {
    private val currentChannelSubject = BehaviorSubject.create<Int>()

    fun maybeAddTimestamp(
        message: Spanned,
        userId: Int,
        messageTimestamp: Int
    ): Spanned {
        if (!Flag.CHAT_TIMESTAMPS.asBoolean()) {
            return message
        }

        return if (userId > 0) {
            createTimestampSpanFromChatMessageSpan(
                message,
                Date(messageTimestamp.toLong() * 1000)
            )
        } else {
            message
        }
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

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun hookEmoteSetsFlowable(
        map: Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>>,
        channelId: Integer
    ): Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>> {
        return map.map { pair ->
            emoteProvider.getEmotesMap(channelId.toInt()).filter { it.second.isNotEmpty() }
                .forEach { emotePair ->
                    pair.second.add(
                        EmoteUiSet(
                            EmoteHeaderUiModel.EmoteHeaderStringResUiModel(
                                packageTokenToId(emotePair.first),
                                true,
                                EmotePickerSection.ORANGE,
                                false
                            ), emotePair.second.map { emote ->
                                createEmoteUiModel(
                                    emote = emote,
                                    channelId = channelId.toInt(),
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
        return when (Flag.DELETED_MESSAGES.asVariant<DeletedMessages>()) {
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

    fun getOrangeEmotesButton(emotePickerViewDelegate: EmotePickerViewDelegate): ImageView? {
        return viewFactory.createOrangeEmotesButton(emotePickerViewDelegate)
    }

    fun rebuildEmotes() {
        Logger.debug("called")
        emoteProvider.rebuild()
    }

    fun renderEmotePickerState(
        state: EmotePickerPresenter.EmotePickerState,
        button: ImageView?
    ) {
        button?.isSelected = state.selectedEmotePickerSection == EmotePickerSection.ORANGE
    }

    companion object {
        private const val TIMESTAMP_DATE_FORMAT = "HH:mm"

        @JvmStatic
        fun get() = Core.getFeature(ChatHookProvider::class.java)

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

            return ResourceManager.get().getStringId(resName)
        }

        @JvmStatic
        fun enableStickyHeaders(): Boolean {
            return !Flag.DISABLE_STICKY_HEADERS_EP.asBoolean()
        }

        @JvmStatic
        fun changeBitsButtonVisibility(org: Boolean): Boolean {
            if (Flag.HIDE_BITS_BUTTON.asBoolean()) {
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

        private fun formatTimestamp(msg: Spanned, timestamp: CharSequence): Spanned =
            SpannableString.valueOf(SpannableStringBuilder(timestamp).append(" ").append(msg))

        private fun createTimestampSpanFromChatMessageSpan(msg: Spanned, date: Date): Spanned =
            formatTimestamp(
                msg = msg,
                timestamp = SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.ENGLISH).format(date)
            )
    }

    override fun onDestroyFeature() {
        PreferenceManager.get().unregisterFlagListeners(this)
        Core.get().unregisterLifecycleListener(this)
        onAllComponentDestroyed()
    }

    override fun onCreateFeature() {
        Core.get().registerLifecycleListeners(this)
        PreferenceManager.get().registerFlagListeners(this)
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
        Logger.debug("channelId: $channelId")
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
}

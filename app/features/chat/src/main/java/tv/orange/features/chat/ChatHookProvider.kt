package tv.orange.features.chat

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.BehaviorSubject
import org.reactivestreams.Publisher
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.core.models.FlagListener
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
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
import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteSet
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteHeaderUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmotePickerSection
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiSet
import tv.twitch.android.shared.emotes.models.EmoteMessageInput
import javax.inject.Inject

class ChatHookProvider @Inject constructor(
    val emoteProvider: EmoteProvider,
    val badgeProvider: BadgeProvider
) : LifecycleAware, FlagListener {
    private val currentChannelSubject = BehaviorSubject.create<Int>()

    companion object {
        private val INSTANCE: ChatHookProvider by lazy {
            val hook = DaggerChatComponent.builder()
                .badgesComponent(Core.get().provideComponent(BadgesComponent::class).get())
                .emotesComponent(Core.get().provideComponent(EmotesComponent::class).get())
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
    }

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
        emoteProvider.refreshGlobalEmotes()
    }

    override fun onFirstActivityCreated() {
        emoteProvider.fetchGlobalEmotes()
        badgeProvider.fetchBadges()
    }

    override fun onConnectingToChannel(channelId: Int) {
        emoteProvider.requestChannelEmotes(channelId)
        currentChannelSubject.onNext(channelId)
    }

    override fun onFlagChanged(flag: Flag) {
        when (flag) {
            Flag.BTTV_EMOTES, Flag.FFZ_EMOTES, Flag.STV_EMOTES -> {
                emoteProvider.clear()
                emoteProvider.fetchGlobalEmotes()
            }
            Flag.FFZ_BADGES, Flag.STV_BADGES, Flag.CHA_BADGES, Flag.CHE_BADGES -> {
                badgeProvider.clear()
                badgeProvider.fetchBadges()
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

    fun hookAutoCompleteMapProvider(flow: Flowable<MutableList<EmoteSet>>): Flowable<MutableList<EmoteSet>> {
        return flow.flatMap { org ->
            currentChannelSubject.toFlowable(BackpressureStrategy.LATEST).flatMap { currentChannelId ->
                val injected = emoteProvider.getEmotesMap(currentChannelId).map { pair ->
                    val emotes = pair.second.map {
                        OrangeEmoteModel(
                            it.getCode(),
                            it.getUrl(Emote.Size.MEDIUM)
                        ) as EmoteModel
                    }.toMutableList()
                    OrangeEmoteSet(emotes)
                }.toMutableList()
                val res = (org + injected) as MutableList<EmoteSet>
                Logger.debug("res: $res")
                Flowable.just(res)
            }
        }
    }
}

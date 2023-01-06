package tv.orange.features.chat

import android.content.Context
import android.text.Spanned
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.core.PreferenceManager.Companion.isDarkThemeEnabled
import tv.orange.core.ResourceManager
import tv.orange.core.compat.ClassCompat.getPrivateField
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.flag.variants.DeletedMessages
import tv.orange.core.models.flag.variants.EmoteQuality
import tv.orange.core.models.flag.variants.FontSize
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.features.badges.bridge.OrangeMessageBadge
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.blacklist.Blacklist
import tv.orange.features.chat.bridge.*
import tv.orange.features.chat.data.model.FavEmote
import tv.orange.features.chat.data.model.OrangeFavEmote
import tv.orange.features.chat.data.model.TwitchFavEmote
import tv.orange.features.chat.data.repository.FavEmotesRepository
import tv.orange.features.chat.util.ChatUtil
import tv.orange.features.chat.util.ChatUtil.createDeletedGrey
import tv.orange.features.chat.util.ChatUtil.createDeletedStrikethrough
import tv.orange.features.chat.util.ChatUtil.createTimestampSpanFromChatMessageSpan
import tv.orange.features.chat.util.ChatUtil.isUserMentioned
import tv.orange.features.chat.util.ChatUtil.spToPx
import tv.orange.features.chat.view.ViewFactory
import tv.orange.features.emotes.bridge.EmoteToken
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.highlighter.Highlighter
import tv.orange.features.pronouns.PronounSetter
import tv.orange.features.pronouns.component.PronounProvider
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.EmoteCardModelWrapper
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.abc.Feature
import tv.orange.models.data.emotes.Emote
import tv.twitch.android.core.adapters.RecyclerAdapterItem
import tv.twitch.android.core.user.TwitchAccountManager
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.models.emotes.EmoteCardModelResponse
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteSet
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.provider.chat.events.MessagesReceivedEvent
import tv.twitch.android.shared.chat.adapter.item.ChatMessageClickedEvents
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem
import tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model.CreatorPinnedChatMessageChannelModel
import tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model.CreatorPinnedChatMessageMessageModel
import tv.twitch.android.shared.chat.network.creatorpinnedchatmessage.model.CreatorPinnedChatMessagePinnedByUserModel
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerViewDelegate
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteHeaderUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmotePickerSection
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiSet
import tv.twitch.android.shared.ui.elements.span.UrlDrawable
import tv.twitch.android.shared.ui.elements.span.`MediaSpan$Type`
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AutoInitialize
class ChatHookProvider @Inject constructor(
    val context: Context,
    val emoteProvider: EmoteProvider,
    val badgeProvider: BadgeProvider,
    val pronounProvider: PronounProvider,
    val viewFactory: ViewFactory,
    val twitchAccountManager: TwitchAccountManager,
    val supportBridge: SupportBridge,
    val favEmotesRepository: FavEmotesRepository,
    val chatFactory: ChatFactory,
    val highlighter: Highlighter,
    val blacklist: Blacklist
) : LifecycleAware, FlagListener, Feature, SupportBridge.Callback {
    private val currentChannelSubject = BehaviorSubject.create<Int>()

    var emoteSize: Emote.Size = Emote.Size.MEDIUM
    var pronouns: Boolean = false

    override fun onAllComponentStopped() {}
    override fun onAccountLogout() {}
    override fun onFirstActivityStarted() {}
    override fun onConnectedToChannel(channelId: Int) {}

    fun hookMessageInterface(
        cmi: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        val badges = injectBadges(
            badges = cmi.badges,
            userId = cmi.userId,
            channelId = channelId
        )
        val tokens = injectEmotes(
            tokens = cmi.tokens,
            userId = cmi.userId,
            channelId = channelId
        )

        return ChatMessageInterfaceWrapper(
            cmi = cmi,
            badges = badges.toMutableList(),
            tokens = tokens.toMutableList()
        )
    }

    private fun injectBadges(
        badges: List<MessageBadge>,
        userId: Int,
        channelId: Int
    ): List<MessageBadge> {
        if (!badgeProvider.hasBadges(userId = userId)) {
            return badges
        }

        val newBadges = badgeProvider.getBadges(userId = userId).toMutableList()
        if (newBadges.isEmpty()) {
            return badges
        }

        val stack = mutableListOf<MessageBadge>()
        badges.forEach { badge ->
            val replaces = newBadges.firstOrNull { it.getReplaces() == badge.name }

            if (replaces != null) {
                stack.add(
                    OrangeMessageBadge(
                        badgeName = replaces.getCode(),
                        badgeUrl = replaces.getUrl(),
                        badgeBackgroundColor = replaces.getBackgroundColor()
                    )
                )
                newBadges.remove(replaces)
            } else {
                stack.add(badge)
            }
        }
        stack.addAll(newBadges.map {
            OrangeMessageBadge(
                badgeName = it.getCode(),
                badgeUrl = it.getUrl(),
                badgeBackgroundColor = it.getBackgroundColor()
            )
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
                    val emote = emoteProvider.getEmote(code = word, channelId = channelId)
                    if (emote != null) {
                        if (!injected) {
                            injected = true
                        }
                        stack.add(
                            EmoteToken(
                                emoteCode = emote.getCode(),
                                emoteUrl = emote.getUrl(emoteSize),
                                emoteCardUrl = emote.getUrl(Emote.Size.LARGE),
                                packageSet = emote.getPackageSet(),
                                isZeroWidth = emote.isZeroWidth()
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
            return tryZwEmoteTokens(stack)
        }

        return tokens
    }

    private fun tryZwEmoteTokens(tokens: Collection<MessageToken>): MutableList<MessageToken> {
        val newTokens = mutableListOf<MessageToken>()

        var stack: StackEmoteToken? = null
        for (token in tokens) {
            when (token) {
                is MessageToken.TextToken -> {
                    if (!token.text.isNullOrBlank()) {
                        stack = null
                    }
                    newTokens.add(token)
                }
                is MessageToken.EmoticonToken -> {
                    stack = StackEmoteToken(core = StackEmoteToken.TokenHolder(token))
                    newTokens.add(stack)
                }
                is EmoteToken -> {
                    if (!token.isZeroWidth) {
                        stack = StackEmoteToken(core = StackEmoteToken.TokenHolder(token))
                        newTokens.add(stack)
                    } else {
                        stack?.stack?.add(token) ?: newTokens.add(token)
                    }
                }
                else -> {
                    stack = null
                    newTokens.add(token)
                }
            }
        }

        return newTokens
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    private fun injectThirdPartyUISets(
        map: Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>>,
        channelId: Integer?
    ): Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>> {
        return map.map { pair ->
            emoteProvider.getEmotesMap(channelId = channelId?.toInt() ?: 0)
                .filter { it.second.isNotEmpty() }
                .forEach { emotePair ->
                    pair.second.add(
                        EmoteUiSet(
                            EmoteHeaderUiModel.EmoteHeaderStringResUiModel(
                                packageTokenToId(emotePair.first),
                                true,
                                EmotePickerSection.ORANGE,
                                false
                            ), emotePair.second.map { emote ->
                                chatFactory.createEmoteUiModel(
                                    emote = emote,
                                    channelId = channelId?.toInt() ?: 0,
                                    isAnimated = false,
                                    packageSet = emote.getPackageSet()
                                )
                            })
                    )
                }

            return@map pair
        }
    }

    private fun mapDBEmotesToUiSet(
        entities: List<FavEmote>,
        channelId: Int
    ): List<EmoteUiModel> {
        return entities.filter {
            it.getChannelId() == -1 || it.getChannelId() == channelId
        }.mapNotNull {
            when (it) {
                is TwitchFavEmote -> {
                    chatFactory.createFavEmoteUiModel(
                        emoteToken = it.getCode(),
                        emoteId = it.emoteId,
                        channelId = it.getChannelId(),
                        isAnimated = it.isAnimated(),
                        packageSet = it.getPackageSet()
                    )
                }
                is OrangeFavEmote -> {
                    val emote = emoteProvider.getEmote(
                        code = it.getCode(),
                        channelId = it.getChannelId(),
                        emotePackageSet = it.getPackageSet()
                    ) ?: return@mapNotNull null

                    chatFactory.createOrangeFavEmoteUiModel(
                        emote.getUrl(Emote.Size.LARGE),
                        emoteCode = it.getCode(),
                        emoteId = "",
                        channelId = it.getChannelId(),
                        animated = it.isAnimated(),
                        packageSet = it.getPackageSet()
                    )
                }
            }
        }
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun hookEmoteSetsFlowable(
        map: Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>>,
        channelId: Integer?
    ): Flowable<Pair<EmoteUiSet, MutableList<EmoteUiSet>>> {
        return injectThirdPartyUISets(map, channelId).flatMap { pair ->
            favEmotesRepository.getChannelEmotes(channelId = channelId?.toInt() ?: -1).observeOn(
                Schedulers.computation()
            ).map {
                mapDBEmotesToUiSet(it, channelId?.toInt() ?: -1)
            }.map {
                chatFactory.createFavEmoteUiSet(it)
            }.observeOn(AndroidSchedulers.mainThread()).map { set: EmoteUiSet ->
                pair.second.find { it.header.emotePickerSection.equals(EmotePickerSection.FAV) }
                    ?.let {
                        pair.second.remove(it)
                    }
                if (set.emotes.isNotEmpty()) {
                    pair.second.add(0, set)
                }
                pair
            }.toFlowable()
        }
    }

    fun hookEmoteCardModelResponse(emoteId: String?): EmoteCardModelResponse? {
        if (emoteId.isNullOrBlank()) {
            return null
        }

        val model = EmoteCardModelWrapper.fromString(str = emoteId) ?: return null

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
            if (orgList.isEmpty()) {
                return@flatMap Flowable.empty()
            }

            currentChannelSubject.flatMap {
                Observable.just(it).delay(DELAY_BEFORE_INJECT, TimeUnit.SECONDS)
            }.toFlowable(BackpressureStrategy.LATEST).flatMap { channelId ->
                val newSets = emoteProvider.getEmotesMap(channelId = channelId).map { pair ->
                    OrangeEmoteSet(orangeEmotes = pair.second.map {
                        OrangeEmoteModel(
                            emoteToken = it.getCode(),
                            emoteUrl = it.getUrl(Emote.Size.MEDIUM)
                        )
                    })
                }
                Flowable.just((orgList + newSets))
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
            DeletedMessages.Mod -> tv.twitch.android.shared.chat.util.ChatUtil.Companion!!.createDeletedSpanFromChatMessageSpan(
                messageId,
                message,
                context,
                eventDispatcher,
                true
            )
            DeletedMessages.Strikethrough -> createDeletedStrikethrough(message = message)
            DeletedMessages.Grey -> createDeletedGrey(message = message)
            DeletedMessages.Default -> tv.twitch.android.shared.chat.util.ChatUtil.Companion!!.createDeletedSpanFromChatMessageSpan(
                messageId,
                message,
                context,
                eventDispatcher,
                hasModAccess
            )
        }
    }

    fun getOrangeEmotesButton(delegate: EmotePickerViewDelegate): ImageView {
        return viewFactory.createOrangeEmotesButton(delegate = delegate)
    }

    fun rebuildEmotes() {
        emoteProvider.rebuild()
    }

    fun renderEmotePickerState(
        state: EmotePickerPresenter.EmotePickerState,
        button: ImageView?
    ) {
        button?.isSelected = state.selectedEmotePickerSection == EmotePickerSection.ORANGE
    }

    companion object {
        private const val DELAY_BEFORE_INJECT = 3L

        var fontSizeSp: Int = 0
        var fontSizePx: Float = 0F
        var fontSizeScaleFactory: Float = 0F

        var chatTimestamps: Boolean = false

        private var killChat = false

        @JvmStatic
        fun get() = Core.getFeature(ChatHookProvider::class.java)

        @JvmStatic
        fun bypassChatBan(): Boolean {
            return Flag.BYPASS_CHAT_BAN.asBoolean()
        }

        @JvmStatic
        fun hideChatHeader(): Boolean {
            return Flag.HIDE_CHAT_HEADER.asBoolean()
        }

        @JvmStatic
        fun sortEmoteSets(list: MutableList<EmoteUiSet>): MutableList<EmoteUiSet> {
            list.find { it.header.emotePickerSection.equals(EmotePickerSection.FAV) }?.let {
                list.remove(it)
                list.add(0, it)
            }

            return list
        }

        private fun packageTokenToId(token: EmotePackageSet): Int {
            val resName = when (token) {
                EmotePackageSet.BttvGlobal -> "orange_bttv_global_emotes"
                EmotePackageSet.BttvChannel -> "orange_bttv_channel_emotes"
                EmotePackageSet.FfzGlobal -> "orange_ffz_global_emotes"
                EmotePackageSet.FfzChannel -> "orange_ffz_channel_emotes"
                EmotePackageSet.StvGlobal -> "orange_stv_global_emotes"
                EmotePackageSet.StvChannel -> "orange_stv_channel_emotes"
                EmotePackageSet.ItzChannel -> "orange_itz_channel_emotes"
                EmotePackageSet.ItzGlobal -> "orange_itz_global_emotes"
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

        private fun calcFontSizeScale(fontSizeSp: Int): Float {
            return fontSizeSp.div(FontSize.SP13.value.removeSuffix("sp").toFloat())
        }

        @JvmStatic
        fun hookMediaSpanSizeDp(sizeDp: Float): Float {
            if (fontSizeScaleFactory != 0F) {
                return sizeDp * fontSizeScaleFactory
            }

            return sizeDp
        }

        @JvmStatic
        fun fixUsernameSpanColor(usernameColor: Int): Int {
            return ChatUtil.fixUsernameColor(
                color = usernameColor,
                isDarkThemeEnabled = isDarkThemeEnabled
            )
        }

        @JvmStatic
        fun maybeAddTimestamp(
            message: Spanned,
            userId: Int,
            messageTimestamp: Int
        ): Spanned {
            if (!chatTimestamps) {
                return message
            }

            return if (userId > 0) {
                createTimestampSpanFromChatMessageSpan(
                    message = message,
                    date = Date(messageTimestamp.toLong() * 1000)
                )
            } else {
                message
            }
        }

        @JvmStatic
        fun fixDeletedMessage(
            ret: IMessageRecyclerItem,
            cmi: ChatMessageInterface
        ) {
            if (cmi.isDeleted && !ret.getHasBeenDeleted()) {
                ret.markAsDeleted()
            }
        }

        @JvmStatic
        val isChatKilled
            get() = killChat

        @JvmStatic
        val preventModClear
            get() = Flag.PREVENT_MOD_CLEAR.asBoolean()
    }

    private fun updateFontSize() {
        fontSizeSp = Flag.CHAT_FONT_SIZE.asString().removeSuffix("sp").toInt()
        fontSizePx = spToPx(context = context, sp = fontSizeSp)
        fontSizeScaleFactory = calcFontSizeScale(fontSizeSp = fontSizeSp)
    }

    override fun onAllComponentDestroyed() {
        emoteProvider.clear()
        badgeProvider.clear()
        pronounProvider.destroy()
    }

    override fun onSdkResume() {
        badgeProvider.refreshBadges()
        emoteProvider.fetch()
    }

    override fun onFirstActivityCreated() {
        badgeProvider.fetchBadges()
        emoteProvider.fetch()
        pronounProvider.fetchPronouns()
    }

    override fun onConnectingToChannel(channelId: Int) {
        emoteProvider.requestChannelEmotes(channelId)
        currentChannelSubject.onNext(channelId)
    }

    override fun onFlagValueChanged(flag: Flag) {
        when (flag) {
            Flag.BTTV_EMOTES, Flag.FFZ_EMOTES, Flag.STV_EMOTES, Flag.ITZ_EMOTES -> emoteProvider.rebuild()
            Flag.FFZ_BADGES, Flag.STV_BADGES, Flag.CHA_BADGES, Flag.CHE_BADGES -> badgeProvider.rebuild()
            Flag.CHAT_FONT_SIZE -> {
                updateFontSize()
            }
            Flag.EMOTE_QUALITY -> {
                emoteSize = flag.asVariant<EmoteQuality>().toSize()
            }
            Flag.CHAT_TIMESTAMPS -> {
                chatTimestamps = flag.asBoolean()
            }
            Flag.PRONOUNS -> {
                pronouns = flag.asBoolean()
            }
            else -> {}
        }
    }

    fun setShouldHighlightBackground(
        message: IMessageRecyclerItem,
        cmi: ChatMessageInterface
    ) {
        if (highlighter.isEnabled()) {
            highlighter.getHighlightDesc(cmi)?.let { desc ->
                message.setHighlightColor(desc.color)
                if (desc.vibrate) {
                    vibrate()
                }
                return
            }
        }

        if (isUserMentioned(cmi = cmi, username = twitchAccountManager.username)) {
            message.setHighlightColor(Flag.USER_MENTION_COLOR.asInt())
            if (Flag.VIBRATE_ON_MENTION.asBoolean()) {
                vibrate()
            }
        }
    }

    private fun vibrate() {
        Core.vibrate(
            context = context,
            delay = 200,
            duration = Flag.VIBRATION_DURATION.asInt()
        )
    }

    override fun maybeChangeMessageFontSize(textView: TextView) {
        if (fontSizeScaleFactory == 0f) {
            return
        }

        if (fontSizeSp == 0) {
            updateFontSize()
        }

        if (textView.textSize == fontSizePx) {
            return
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSizePx)
    }

    override fun bindHighlightMessage(
        vh: RecyclerView.ViewHolder,
        highlightColor: Int?
    ) {
        highlightColor?.let { color ->
            vh.itemView.setBackgroundColor(color)
        } ?: run {
            vh.itemView.background = null
        }
    }

    fun onBindToViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: RecyclerAdapterItem
    ) {
        supportBridge.onBindToViewHolder(viewHolder, item, this)
    }

    fun bindPronoun(
        holder: MessageRecyclerItem.ChatMessageViewHolder,
        item: RecyclerAdapterItem
    ): PronounSetter? {
        if (!pronouns) {
            return null
        }

        if (item !is MessageRecyclerItem) {
            return null
        }

        val userName = item.getPrivateField<String?>("username") ?: return null
        val setter = PronounSetter(view = holder)

        pronounProvider.getPronounText(userName = userName) { pronounText: String ->
            setter.setText(pronounText)
        }

        return setter
    }

    fun hookEmotePickerPresenterLongEmoteClick(clickEvent: EmotePickerPresenter.ClickEvent): Boolean {
        if (clickEvent !is EmotePickerPresenter.ClickEvent.LongClick) {
            return false
        }

        val clickedEmote = clickEvent.getClickedEmote()
        if (clickedEmote !is EmotePickerPresenter.ClickedEmote.Unlocked) {
            return false
        }

        val model = clickedEmote.emote ?: return false

        if (model is EmotePickerEmoteModelExt) {
            if (model is EmotePickerEmoteModelExt.EmotePickerEmoteModelFav) {
                favEmotesRepository.deleteEmote(
                    type = model.packageSet.name,
                    channelId = model.channelId.toString(),
                    code = model.token
                )
                Toast.makeText(
                    Core.get().context,
                    "Deleted: ${model.token}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                favEmotesRepository.addEmote(
                    OrangeFavEmote(
                        code = model.token,
                        isAnimated = model.assetType == EmoteModelAssetType.ANIMATED,
                        channelId = model.channelId,
                        packageSet = model.packageSet
                    )
                )
                Toast.makeText(Core.get().context, "Added: ${model.token}", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            favEmotesRepository.addEmote(
                TwitchFavEmote(
                    code = model.token,
                    isAnimated = model.assetType == EmoteModelAssetType.ANIMATED,
                    channelId = clickedEmote.trackingMetadata?.chatChannelId ?: -1,
                    packageSet = EmotePackageSet.TwitchChannel,
                    emoteId = model.id
                )
            )
            Toast.makeText(Core.get().context, "Added: ${model.token}", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    fun hookPinnedMessage(
        pinnedMessage: CreatorPinnedChatMessageMessageModel,
        channel: CreatorPinnedChatMessageChannelModel,
        pinnedByUser: CreatorPinnedChatMessagePinnedByUserModel
    ): CreatorPinnedChatMessageMessageModel {
        return channel.channelId.toIntOrNull()?.let { channelId ->
            pinnedByUser.getPrivateField<String>("pinnedByUserId").toIntOrNull()?.let { userId ->
                CreatorPinnedChatMessageMessageModel(
                    pinnedMessage.pinnedMessageId,
                    pinnedMessage.getPrivateField("pinnedMessageText"),
                    injectEmotes(pinnedMessage.tokens, userId, channelId)
                )
            }
        } ?: pinnedMessage
    }

    fun switchKillChat() {
        killChat = !killChat
    }

    override fun onCreateFeature() {
        Core.get().registerLifecycleListeners(this)
        PreferenceManager.get().registerFlagListeners(this)
        updateFontSize()
        emoteSize = Flag.EMOTE_QUALITY.asVariant<EmoteQuality>().toSize()
        pronouns = Flag.PRONOUNS.asBoolean()
        chatTimestamps = Flag.CHAT_TIMESTAMPS.asBoolean()
        highlighter.pull()
        blacklist.pull()
    }


    fun filterMessages(flowable: Observable<MessagesReceivedEvent>): Observable<MessagesReceivedEvent> {
        return flowable.map { mre ->
            if (!blacklist.isEnabled()) {
                return@map mre
            }

            MessagesReceivedEvent(
                mre.channelId,
                mre.messages.filter { message -> !blacklist.isBlacklisted(message) })
        }.filter { it.messages.isNotEmpty() }
    }

    fun urlDrawableFactory(url: String, animatedEmotesEnabled: Boolean): UrlDrawable {
        return UrlDrawable(url, `MediaSpan$Type`.Emote, true, animatedEmotesEnabled)
    }
}

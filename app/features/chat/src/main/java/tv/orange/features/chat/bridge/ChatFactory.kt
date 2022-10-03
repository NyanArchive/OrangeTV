package tv.orange.features.chat.bridge

import tv.orange.core.ResourceManager
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.abc.OrangeEmoteType
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter
import tv.twitch.android.shared.emotes.emotepicker.models.*
import tv.twitch.android.shared.emotes.models.EmoteMessageInput
import javax.inject.Inject

class ChatFactory @Inject constructor(
    val resourceManager: ResourceManager
) {
    fun createFavEmoteUiModel(
        uid: Int,
        emoteCode: String,
        emoteId: String,
        channelId: Int,
        animated: Boolean,
        packageSet: EmotePackageSet
    ): EmoteUiModel {
        val emoteMessageInput = EmoteMessageInput(emoteCode, emoteId, true)
        val picker = EmotePickerEmoteModelExt.EmotePickerEmoteModelFav(
            uid = uid,
            id = emoteId,
            token = emoteCode,
            channelId = channelId,
            isAnimated = animated,
            isTwitchEmote = packageSet.type == OrangeEmoteType.TWITCH,
            isGlobalEmote = packageSet.isGlobal,
            packageSet = packageSet
        )
        return EmoteUiModel(
            emoteId,
            EmotePickerPresenter.ClickedEmote.Unlocked(
                picker,
                emoteMessageInput,
                null,
                emptyList()
            ),
            if (animated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
            if (animated) EmoteImageDescriptor.ANIMATED else EmoteImageDescriptor.NONE
        )
    }

    fun createFavEmoteUiSet(
        model: List<EmoteUiModel?>
    ): EmoteUiSet {
        val header = EmoteHeaderUiModel.EmoteHeaderStringResUiModel(
            resourceManager.getStringId("orange_emote_picker_fav_emotes"),
            true,
            EmotePickerSection.FAV,
            false
        )
        return EmoteUiSet(header, model)
    }

    fun createOrangeFavEmoteUiModel(
        url: String,
        uid: Int,
        emoteCode: String,
        emoteId: String,
        channelId: Int,
        animated: Boolean,
        packageSet: EmotePackageSet
    ): EmoteUiModel {
        val emoteMessageInput = EmoteMessageInput(emoteCode, emoteId, false)
        val emotePicker =
            EmotePickerEmoteModelExt.EmotePickerEmoteModelFav(
                uid = uid,
                id = emoteId,
                token = emoteCode,
                channelId = channelId,
                isAnimated = animated,
                isTwitchEmote = packageSet.type == OrangeEmoteType.TWITCH,
                isGlobalEmote = packageSet.isGlobal,
                packageSet = packageSet
            )
        val clickedEmote = EmotePickerPresenter.ClickedEmote.Unlocked(
            emotePicker,
            emoteMessageInput,
            null,
            emptyList()
        )
        return EmoteUiModelExt.EmoteUiModelWithUrl(emoteId, clickedEmote, url, animated)
    }
}
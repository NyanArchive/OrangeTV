package tv.orange.features.chat.bridge

import tv.orange.core.ResourceManager
import tv.orange.models.abc.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter
import tv.twitch.android.shared.emotes.emotepicker.models.*
import tv.twitch.android.shared.emotes.models.EmoteMessageInput
import javax.inject.Inject

class ChatFactory @Inject constructor(
    val resourceManager: ResourceManager
) {
    fun createFavEmoteUiModel(
        emoteId: String,
        emoteToken: String,
        isAnimated: Boolean,
        channelId: Int,
        packageSet: EmotePackageSet
    ): EmoteUiModel {
        val emoteMessageInput = EmoteMessageInput(emoteToken, emoteId, true)
        val picker = EmotePickerEmoteModelExt.EmotePickerEmoteModelFav(
            emoteId = emoteId,
            emoteToken = emoteToken,
            isAnimated = isAnimated,
            channelId = channelId,
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
            if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
            if (isAnimated) EmoteImageDescriptor.ANIMATED else EmoteImageDescriptor.NONE
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
        emoteCode: String,
        emoteId: String,
        channelId: Int,
        animated: Boolean,
        packageSet: EmotePackageSet
    ): EmoteUiModel {
        val emoteMessageInput = EmoteMessageInput(emoteCode, emoteId, false)
        val emotePicker =
            EmotePickerEmoteModelExt.EmotePickerEmoteModelFav(
                emoteId = emoteId,
                emoteToken = emoteCode,
                isAnimated = animated,
                channelId = channelId,
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

    fun createEmoteUiModel(
        emote: Emote,
        channelId: Int,
        isAnimated: Boolean,
        packageSet: EmotePackageSet
    ): EmoteUiModel {
        val emoteMessageInput = EmoteMessageInput(emote.getCode(), "-1", false)
        val emotePicker = EmotePickerEmoteModelExt(
            emoteId = "-1",
            emoteToken = emote.getCode(),
            isAnimated = isAnimated,
            channelId = channelId,
            packageSet = packageSet
        )
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
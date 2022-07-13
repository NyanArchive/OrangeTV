package tv.orange.features.chat.bridge

import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerPresenter.ClickedEmote
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteImageDescriptor
import tv.twitch.android.shared.emotes.emotepicker.models.EmoteUiModel

open class EmoteUiModelExt(id: String?, clickedEmote: ClickedEmote?, isAnimated: Boolean) :
    EmoteUiModel(
        id,
        clickedEmote,
        if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
        if (isAnimated) EmoteImageDescriptor.ANIMATED else EmoteImageDescriptor.NONE
    ) {
    class EmoteUiModelWithUrl(
        id: String?, clickedEmote: ClickedEmote?,
        val url: String, isAnimated: Boolean
    ) : EmoteUiModelExt(id, clickedEmote, isAnimated)
}
package tv.orange.features.chat.bridge

import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteModelType

open class EmotePickerEmoteModelUrl(
    id: String?,
    token: String?,
    val url: String,
    isAnimated: Boolean
) : EmoteModel.Generic(
    id,
    token,
    if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
    EmoteModelType.OTHER
)
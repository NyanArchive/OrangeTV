package tv.orange.features.chat.bridge

import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteModelType

open class EmotePickerEmoteModelExt(
    id: String?,
    token: String?,
    val channelId: Int,
    isAnimated: Boolean
) : EmoteModel.Generic(
    id,
    token,
    if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
    EmoteModelType.OTHER
)
package tv.orange.features.chat.bridge

import tv.orange.models.abc.EmotePackageSet
import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteModelType

open class EmotePickerEmoteModelExt(
    id: String?,
    token: String?,
    val channelId: Int,
    isAnimated: Boolean,
    val packageSet: EmotePackageSet
) : EmoteModel.Generic(
    id,
    token,
    if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
    EmoteModelType.OTHER
) {
    class EmotePickerEmoteModelFav(
        val uid: Int,
        id: String?,
        token: String?,
        channelId: Int,
        isAnimated: Boolean,
        packageSet: EmotePackageSet
    ) : EmotePickerEmoteModelExt(
        id,
        token,
        channelId,
        isAnimated,
        packageSet
    )
}
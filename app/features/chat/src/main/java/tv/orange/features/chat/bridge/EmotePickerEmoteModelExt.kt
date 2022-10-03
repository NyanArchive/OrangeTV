package tv.orange.features.chat.bridge

import tv.orange.models.abc.EmotePackageSet
import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteModelType

open class EmotePickerEmoteModelExt(
    emoteId: String?,
    emoteToken: String?,
    isAnimated: Boolean,
    val channelId: Int,
    val packageSet: EmotePackageSet
) : EmoteModel.Generic(
    emoteId,
    emoteToken,
    if (isAnimated) EmoteModelAssetType.ANIMATED else EmoteModelAssetType.STATIC,
    EmoteModelType.OTHER
) {
    class EmotePickerEmoteModelFav(
        val uid: Int,
        emoteId: String?,
        emoteToken: String?,
        isAnimated: Boolean,
        channelId: Int,
        packageSet: EmotePackageSet
    ) : EmotePickerEmoteModelExt(
        emoteId = emoteId,
        emoteToken = emoteToken,
        isAnimated = isAnimated,
        channelId = channelId,
        packageSet = packageSet
    )
}
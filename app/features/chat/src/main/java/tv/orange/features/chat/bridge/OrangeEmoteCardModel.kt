package tv.orange.features.chat.bridge

import tv.orange.models.abs.EmotePackageSet
import tv.twitch.android.models.emotes.EmoteCardModel
import tv.twitch.android.models.emotes.EmoteModelAssetType

class OrangeEmoteCardModel(
    token: String,
    val url: String,
    val set: EmotePackageSet
) : EmoteCardModel(
    null,
    token,
    EmoteModelAssetType.UNKNOWN,
    OrangeEmoteCardType(),
    null
)
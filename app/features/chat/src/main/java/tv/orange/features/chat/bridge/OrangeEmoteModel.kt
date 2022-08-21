package tv.orange.features.chat.bridge

import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteModelAssetType
import tv.twitch.android.models.emotes.EmoteModelType

class OrangeEmoteModel(
    private val emoteToken: String,
    val emoteUrl: String
) : EmoteModel(null) {
    override fun getAssetType(): EmoteModelAssetType {
        return EmoteModelAssetType.UNKNOWN
    }

    override fun getId(): String {
        return "0"
    }

    override fun getToken(): String {
        return emoteToken
    }

    override fun getType(): EmoteModelType {
        return EmoteModelType.OTHER
    }
}
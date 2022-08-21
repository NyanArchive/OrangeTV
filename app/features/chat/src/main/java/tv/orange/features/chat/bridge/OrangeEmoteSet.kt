package tv.orange.features.chat.bridge

import tv.twitch.android.models.emotes.EmoteModel
import tv.twitch.android.models.emotes.EmoteSet

class OrangeEmoteSet(
    val orangeEmotes: List<EmoteModel>
) : EmoteSet(null) {
    override fun getEmotes(): List<EmoteModel> {
        return orangeEmotes
    }

    override fun getSetId(): String {
        return "0"
    }
}
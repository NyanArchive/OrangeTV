package tv.orange.features.chat.bridge

import tv.orange.core.ResourceManager
import tv.twitch.android.core.strings.StringResource
import tv.twitch.android.shared.chat.emotecard.EmoteCardHeaderUiModel
import tv.twitch.android.shared.chat.emotecard.EmoteCardUiModel

class OrangeEmoteCardUiModel(model: OrangeEmoteCardModel) :
    EmoteCardUiModel(
        EmoteCardHeaderUiModel(
            model.emoteToken,
            StringResource.Companion!!.fromStringId(
                ResourceManager.getId(
                    model.set.stringId,
                    "string"
                )
            ),
            model.url,
            false
        ), null, null, null, null, null, null
    )
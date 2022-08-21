package tv.orange.features.chat.bridge

import tv.orange.core.ResourceManager
import tv.twitch.android.shared.chat.emotecard.EmoteCardHeaderUiModel
import tv.twitch.android.shared.chat.emotecard.EmoteCardUiModel

class OrangeEmoteCardUiModel(
    model: OrangeEmoteCardModel
) : EmoteCardUiModel(
    EmoteCardHeaderUiModel(
        model.emoteToken,
        ResourceManager.get().getStringResource(model.set.resName),
        model.url,
        false
    ), null, null, null, null, null, null
)
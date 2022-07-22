package tv.twitch.android.shared.chat.emotecard;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tv.orange.features.chat.bridge.OrangeEmoteCardModel;
import tv.twitch.android.models.emotes.EmoteCardModel;
import tv.twitch.android.models.emotes.EmoteCardModelResponse;

public class EmoteCardPresenter$bind$1 implements Function1<EmoteCardModelResponse, Unit> {
    final EmoteCardPresenter this$0 = null;

    @Override
    public Unit invoke(EmoteCardModelResponse emoteCardModelResponse) {
        if (emoteCardModelResponse instanceof EmoteCardModelResponse.Success) {
            EmoteCardModel emoteCardModel = ((EmoteCardModelResponse.Success) emoteCardModelResponse).getEmoteCardModel();
            if (emoteCardModel instanceof OrangeEmoteCardModel) { // TODO: __INJECT_CODE
                this$0.pushOrangeEmoteCardLoadedState((OrangeEmoteCardModel) emoteCardModel);
                return Unit.INSTANCE;
            }
        }

        return null;
    }
}

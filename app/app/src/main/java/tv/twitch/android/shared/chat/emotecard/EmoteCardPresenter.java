package tv.twitch.android.shared.chat.emotecard;

import tv.orange.features.chat.bridge.OrangeEmoteCardModel;
import tv.orange.features.chat.bridge.OrangeEmoteCardUiModel;
import tv.twitch.android.core.mvp.presenter.RxPresenter;
import tv.twitch.android.core.mvp.presenter.StateUpdateEvent;

public class EmoteCardPresenter extends RxPresenter<EmoteCardState, EmoteCardViewDelegate> {
    private final EmoteCardPresenter$stateUpdater$1 stateUpdater = null;

    /* ... */

    public void pushOrangeEmoteCardLoadedState(OrangeEmoteCardModel emoteCardModel) { // TODO: __INJECT_METHOD
        stateUpdater.pushStateUpdate(new UpdateEvent.Initialized(new EmoteCardState.Loaded(new OrangeEmoteCardUiModel(emoteCardModel), emoteCardModel, null, null)));
    }

    public static abstract class UpdateEvent implements StateUpdateEvent {
        public static final class Initialized extends UpdateEvent {
            private final EmoteCardState.Loaded loadedState;

            public final EmoteCardState.Loaded getLoadedState() {
                return this.loadedState;
            }

            public Initialized(EmoteCardState.Loaded loadedState) {
                this.loadedState = loadedState;
            }
        }
    }

    /* ... */
}
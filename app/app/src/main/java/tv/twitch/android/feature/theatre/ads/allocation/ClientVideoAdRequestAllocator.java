package tv.twitch.android.feature.theatre.ads.allocation;

import tv.twitch.android.core.mvp.presenter.PresenterAction;
import tv.twitch.android.core.mvp.presenter.PresenterState;
import tv.twitch.android.core.mvp.presenter.StateAndAction;
import tv.twitch.android.core.mvp.presenter.StateMachineKt;
import tv.twitch.android.core.mvp.presenter.StateUpdateEvent;
import tv.twitch.android.models.ads.ClientAdRequestMetadata;
import tv.twitch.android.shared.ads.models.AdRequest;
import tv.twitch.android.shared.ads.models.context.AdSessionManifest;

public class ClientVideoAdRequestAllocator {
    public final void requestAd(ClientAdRequestMetadata clientAdRequestMetadata, AdRequest adRequest, AdSessionManifest adSessionManifest) {  // TODO: __REPLACE_METHOD
    }

    public final StateAndAction<State, Action> maybeRequestAd(State state, Event.AdRequested adRequested) { // TODO: __REPLACE_METHOD
        return StateMachineKt.noAction(state);
    }

    private final StateAndAction<State, Action> maybeRequestAd(State.AdRequested adRequested) { // TODO: __REPLACE_METHOD
        return StateMachineKt.noAction(adRequested);
    }

    public static abstract class State implements PresenterState {
        public static final class AdRequested extends State {}
    }

    public static abstract class Action implements PresenterAction {}

    public static abstract class Event implements StateUpdateEvent {
        public static final class AdRequested extends Event {}
    }
}
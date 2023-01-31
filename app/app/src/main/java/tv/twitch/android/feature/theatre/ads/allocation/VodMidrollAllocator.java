package tv.twitch.android.feature.theatre.ads.allocation;

import tv.twitch.android.core.mvp.presenter.PresenterAction;
import tv.twitch.android.core.mvp.presenter.PresenterState;
import tv.twitch.android.core.mvp.presenter.StateAndAction;
import tv.twitch.android.core.mvp.presenter.StateMachineKt;

public class VodMidrollAllocator {
    public static abstract class State implements PresenterState {
        public static final class Active extends State {

        }
    }

    public static abstract class Action implements PresenterAction {

    }

    public final StateAndAction<State, Action> maybeRequestInsertedAd(State.Active active) { // TODO: __REPLACE_METHOD
        return StateMachineKt.noAction(active);
    }
}

package tv.twitch.android.core.mvp.presenter;

import java.util.List;

import tv.orange.core.LoggerImpl;

public class StateMachine {
    /* ... */

    private final void debugStateUpdate(String p0, StateUpdateEvent p1, PresenterState p2, PresenterState p3, List p4) { // TODO: __REPLACE_METHOD
        if (p0 != null) {
            LoggerImpl.INSTANCE.debugStateUpdate(p0, p1, p2, p3, p4);
        }
    }

    /* ... */
}

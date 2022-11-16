package tv.twitch.android.core.mvp.presenter;

import java.util.Iterator;
import java.util.List;

import tv.orange.core.LoggerImpl;


public class StateMachine {
    /* ... */

    private final void debugStateUpdate(String p0, StateUpdateEvent p1, PresenterState p2, PresenterState p3, List p4) { // TODO: __REPLACE_METHOD
        if (p0 != null) {
            LoggerImpl.INSTANCE.devDebug(p0 + ":new transition", false);
            LoggerImpl.INSTANCE.devDebug(p0 + ":   event: " + p1, false);
            LoggerImpl.INSTANCE.devDebug(p0 + ":   previous state: " + p2, false);
            LoggerImpl.INSTANCE.devDebug(p0 + ":   new State: " + p3, false);
            Iterator iterator = p4.iterator();
            while (iterator.hasNext()) {
                LoggerImpl.INSTANCE.devDebug(p0 + ":   action: " + iterator.next(), false);
            }
        }
    }

    /* ... */
}

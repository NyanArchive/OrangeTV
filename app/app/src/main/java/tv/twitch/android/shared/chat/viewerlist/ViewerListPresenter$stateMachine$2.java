package tv.twitch.android.shared.chat.viewerlist;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.PresenterAction;
import tv.twitch.android.core.mvp.presenter.StateAndAction;

public class ViewerListPresenter$stateMachine$2 {
    final ViewerListPresenter this$0 = null;

    /* ... */

    public final StateAndAction<ViewerListPresenter.State, PresenterAction> invoke(ViewerListPresenter.State currentState, ViewerListPresenter.UpdateEvent updateEvent) {
        if (updateEvent instanceof ViewerListPresenter.UpdateEvent.SearchTextUpdateEvent) { // TODO: __INJECT_CODE
            return this$0.getStateSearchText(currentState, (ViewerListPresenter.UpdateEvent.SearchTextUpdateEvent) updateEvent);
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

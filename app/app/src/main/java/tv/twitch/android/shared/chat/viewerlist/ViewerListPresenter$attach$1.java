package tv.twitch.android.shared.chat.viewerlist;

import tv.orange.models.exception.VirtualImpl;

public class ViewerListPresenter$attach$1 {
    final ViewerListPresenter this$0 = null;

    /* ... */

    public final void invoke2(ViewerListViewDelegate.Event event) {
        if (event instanceof ViewerListViewDelegate.Event.Search) { // TODO: __INJECT_CODE
            this$0.pushSearchEvent(((ViewerListViewDelegate.Event.Search) event).getText());
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

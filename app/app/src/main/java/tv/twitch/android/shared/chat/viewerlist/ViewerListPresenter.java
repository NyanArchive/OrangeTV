package tv.twitch.android.shared.chat.viewerlist;

import androidx.annotation.NonNull;

import tv.orange.features.usersearch.UserSearch;
import tv.orange.features.usersearch.bridge.IProxyEvent;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.RxPresenter;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;

public class ViewerListPresenter extends RxPresenter implements IProxyEvent { // TODO: @features:usersearch
    private ViewerListAdapterBinder adapterBinder;

    /* ... */

    @Override
    public void pushState(Object o) {
        /* ... */

        throw new VirtualImpl();
    }

    public void attach(ViewerListViewDelegate viewDelegate) {
        /* ... */

        UserSearch.get().setupFilter(viewDelegate, this); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    @Override
    public void proxyEvent(@NonNull ViewDelegateEvent event) { // TODO: __INJECT_METHOD
        if (event instanceof ViewerListViewDelegate.Event.Search) {
            adapterBinder.setSearchUserText(((ViewerListViewDelegate.Event.Search) event).getText());
        }
    }
}

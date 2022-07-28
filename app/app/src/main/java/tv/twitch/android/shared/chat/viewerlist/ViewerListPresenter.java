package tv.twitch.android.shared.chat.viewerlist;

import androidx.annotation.NonNull;

import tv.orange.features.usersearch.Hook;
import tv.orange.features.usersearch.bridge.IProxyEvent;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.RxPresenter;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetBehaviorViewDelegate;

public class ViewerListPresenter extends RxPresenter implements IProxyEvent { // TODO: @features:usersearch
    private ViewerListAdapterBinder adapterBinder;

    /* ... */

    @Override
    public void pushState(Object o) {
        /* ... */

        throw new VirtualImpl();
    }

    public final void attachViewDelegate(ViewerListViewDelegate viewDelegate, BottomSheetBehaviorViewDelegate bottomSheetBehaviorViewDelegate) {
        /* ... */

        Hook.get().setupFilter(viewDelegate, this); // TODO: __INJECT_CODE

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

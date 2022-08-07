package tv.twitch.android.shared.ui.elements.navigation;

import java.util.ArrayList;
import java.util.List;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class BottomNavigationPresenter {
    private final List<BottomNavigationItem> items = new ArrayList();

    /* ... */

    public final void setItems(List<BottomNavigationItem> list) {
        list = UI.get().filterNavItems(list);  // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

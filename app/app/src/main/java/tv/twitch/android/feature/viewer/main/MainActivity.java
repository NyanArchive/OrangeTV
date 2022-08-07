package tv.twitch.android.feature.viewer.main;

import android.os.Bundle;
import android.view.ViewGroup;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class MainActivity {
    private ViewGroup mWrapper;

    /* ... */

    public void onCreate(Bundle bundle) {
        /* ... */

        this.mWrapper = null;
        UI.get().attachToMainActivityWrapper(this.mWrapper); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

package tv.twitch.android.feature.viewer.main;

import android.os.Bundle;
import android.view.ViewGroup;

import tv.orange.features.ui.UI;
import tv.orange.features.updater.Updater;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.activities.TwitchDaggerActivity;

public class MainActivity extends TwitchDaggerActivity {
    private ViewGroup mWrapper;

    /* ... */

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        /* ... */

        this.mWrapper = null;
        UI.get().attachToMainActivityWrapper(this.mWrapper); // TODO: __INJECT_CODE

        /* ... */

        Updater.get().checkUpdates(this, true);

        throw new VirtualImpl();
    }

    /* ... */
}

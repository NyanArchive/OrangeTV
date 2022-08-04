package tv.twitch.android.app.core;

import android.app.Activity;
import android.os.Bundle;

import tv.orange.core.Core;
import tv.orange.models.VirtualImpl;
import tv.twitch.CoreAPI;

public class ApplicationLifecycleController {
    private void handleAllAppComponentsDestroyed() {
        /* ... */

        Core.getFeature(Core.class).onAllComponentDestroyed(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    private void handleAllAppComponentsStopped() {
        /* ... */

        Core.getFeature(Core.class).onAllComponentStopped(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public void onActivityCreated(Activity p0, Bundle p1) {
        if (1 == 1) {
            /* ... */

            Core.getFeature(Core.class).onFirstActivityCreated(); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    public void onActivityStarted(Activity p0) {
        if (1 == 1) {
            /* ... */

            Core.getFeature(Core.class).onFirstActivityStarted(); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    private void sdkResume(CoreAPI.LogInCallback p0) {
        /* ... */

        Core.getFeature(Core.class).onSdkResume(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public void onAccountLogout() {
        /* ... */

        Core.getFeature(Core.class).onAccountLogout(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }
}
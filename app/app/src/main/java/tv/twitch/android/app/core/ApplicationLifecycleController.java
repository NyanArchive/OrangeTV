package tv.twitch.android.app.core;

import android.app.Activity;
import android.os.Bundle;

import tv.orange.core.Core;
import tv.orange.models.VirtualImpl;
import tv.twitch.CoreAPI;

public class ApplicationLifecycleController {
    private void handleAllAppComponentsDestroyed() {
        /* ... */

        Core.get().onAllComponentDestroyed(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    private void handleAllAppComponentsStopped() {
        /* ... */

        Core.get().onAllComponentStopped(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public void onActivityCreated(Activity p0, Bundle p1) {
        if (1 == 1) {
            /* ... */

            Core.get().onFirstActivityCreated(); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    public void onActivityStarted(Activity p0) {
        if (1 == 1) {
            /* ... */

            Core.get().onFirstActivityStarted(); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    private void sdkResume(CoreAPI.LogInCallback p0) {
        /* ... */

        Core.get().onSdkResume(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public void onAccountLogout() {
        /* ... */

        Core.get().onAccountLogout(); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }
}
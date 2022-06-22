package tv.twitch.android.app.core;

import android.app.Activity;
import android.os.Bundle;

import tv.orange.core.Core;
import tv.twitch.CoreAPI;

public class ApplicationLifecycleController {
    private void handleAllAppComponentsDestroyed() {
        /* ... */

        Core.get().onAllComponentDestroyed();
    }

    private void handleAllAppComponentsStopped() {
        /* ... */

        Core.get().onAllComponentStopped();
    }

    public void onActivityCreated(Activity p0, Bundle p1) {
        if (1 == 1) {
            /* ... */

            Core.get().onFirstActivityCreated();
        }
    }

    public void onActivityStarted(Activity p0) {
        if (1 == 1) {
            /* ... */

            Core.get().onFirstActivityStarted();
        }
    }

    private void sdkResume(CoreAPI.LogInCallback p0) {
        /* ... */

        Core.get().onSdkResume();
    }

    public void onAccountLogout(){
        /* ... */

        Core.get().onAccountLogout();
    }
}
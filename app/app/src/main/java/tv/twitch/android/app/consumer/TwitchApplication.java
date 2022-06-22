package tv.twitch.android.app.consumer;

import tv.orange.core.Core;
import tv.orange.features.emotes.Hook;

public class TwitchApplication {
    public void onCreate() {
        /* ... */

        // Inject after twitch dagger
        Core.Companion.initialize(); // TODO: __INJECT_CODE
        initOranges(); // TODO: __INJECT_CODE

        /* ... */
    }

    private void initOranges() { // TODO: __INJECT_METHOD
        Core core = Core.get();
        core.registerLifecycleListener(Hook.get());
    }
}
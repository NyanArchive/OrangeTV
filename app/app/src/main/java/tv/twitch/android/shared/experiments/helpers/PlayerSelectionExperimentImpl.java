package tv.twitch.android.shared.experiments.helpers;

import tv.orange.core.CoreHook;
import tv.twitch.android.models.player.PlayerImplementation;

public class PlayerSelectionExperimentImpl {
    /* ... */

    public PlayerImplementation getPlayerImplementation() {
        /* ... */

        return CoreHook.hookPlayerImplementation(PlayerImplementation.Core); // TODO: __HOOK_RES
    }

    /* ... */
}

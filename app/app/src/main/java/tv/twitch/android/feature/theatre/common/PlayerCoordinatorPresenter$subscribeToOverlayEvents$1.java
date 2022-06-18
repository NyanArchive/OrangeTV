package tv.twitch.android.feature.theatre.common;

import tv.twitch.android.shared.player.overlay.PlayerOverlayEvents;

public class PlayerCoordinatorPresenter$subscribeToOverlayEvents$1 {
    PlayerCoordinatorPresenter this$0;

    public final void invoke(PlayerOverlayEvents event) {
        if (event instanceof PlayerOverlayEvents.Refresh) { // TODO: __INJECT_CODE
            this$0.refreshStream();
            return;
        }
    }
}
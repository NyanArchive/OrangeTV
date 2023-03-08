package tv.twitch.android.feature.theatre.vod;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.shared.player.overlay.PlayerOverlayEvents;

public class VodPresenter$onViewAttached$3 {
    final VodPresenter this$0 = null;

    public final void invoke(PlayerOverlayEvents playerOverlayEvents) {
        if (playerOverlayEvents instanceof PlayerOverlayEvents.ChangePlaybackSpeed) { // TODO: __INJECT_CODE
            this.this$0.tryChangeSpeed(((PlayerOverlayEvents.ChangePlaybackSpeed) playerOverlayEvents).getSpeed());
            return;
        }

        /* ... */

        throw new VirtualImpl();
    }
}

package tv.twitch.android.feature.theatre.common;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.shared.player.presenters.PlayerPresenter;

public class PlayerCoordinatorPresenter {
    private PlayerPresenter playerPresenter;

    /* ... */

    private void playWithCurrentModeAndQuality() {
        throw new VirtualImpl();
    }

    public final void refreshStream() { // TODO: __INJECT_METHOD
        playerPresenter.stop();
        playWithCurrentModeAndQuality();
    }

    /* ... */
}

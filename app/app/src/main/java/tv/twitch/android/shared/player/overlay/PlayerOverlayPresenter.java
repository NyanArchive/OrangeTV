package tv.twitch.android.shared.player.overlay;

import tv.twitch.android.models.videos.VodModel;
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter;

public class PlayerOverlayPresenter {
    private PlayerOverlayViewDelegate viewDelegate;

    /* .. */

    public void onBindVodModel(VodModel model, SeekbarOverlayPresenter presenter) { // TODO: __INJECT_METHOD
        viewDelegate.onBindVodModel(model, presenter);
    }

    public void hideChaptersButton() { // TODO: __INJECT_METHOD
        viewDelegate.hideChaptersButton();
    }
}

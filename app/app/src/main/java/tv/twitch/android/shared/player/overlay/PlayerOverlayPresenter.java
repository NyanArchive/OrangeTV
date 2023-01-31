package tv.twitch.android.shared.player.overlay;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.videos.VodModel;
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter;
import tv.twitch.android.shared.player.overlay.stream.StreamOverlayConfiguration;

public class PlayerOverlayPresenter {
    private PlayerOverlayViewDelegate viewDelegate;

    /* .. */

    public void onBindVodModel(VodModel model, SeekbarOverlayPresenter presenter) { // TODO: __INJECT_METHOD
        viewDelegate.onBindVodModel(model, presenter);
    }

    public void hideChaptersButton() { // TODO: __INJECT_METHOD
        viewDelegate.hideChaptersButton();
    }

    public final void layoutForOverlayConfiguration() {
        StreamOverlayConfiguration streamOverlayConfiguration = null;

        /* ... */

        UI.get().maybeHideLiveShareButton(streamOverlayConfiguration, viewDelegate); // TODO: __INJECT_METHOD

        /* ... */

        throw new VirtualImpl();
    }
}
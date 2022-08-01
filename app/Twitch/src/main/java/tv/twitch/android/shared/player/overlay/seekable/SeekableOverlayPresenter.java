package tv.twitch.android.shared.player.overlay.seekable;

public class SeekableOverlayPresenter {
    private SeekbarOverlayViewDelegate seekbarOverlayViewDelegate;

    /* ... */

    public void xSeekTo(int seconds) { // TODO: __INJECT_METHOD
        seekbarOverlayViewDelegate.xSeekTo(seconds);
    }

    /* ... */
}

package tv.twitch.android.shared.player.core;

import com.amazonaws.ivs.player.MediaPlayer;

import tv.orange.models.exception.VirtualImpl;

public class TrackingMediaPlayer {
    private final MediaPlayer mediaPlayer = null;

    /* ... */

    public final void setMaxPlaybackRate(int i) {
        throw new VirtualImpl();
    }

    public final void setPlaybackRate(float rate) { // TODO: __INJECT_METHOD
        this.mediaPlayer.setPlaybackRate(rate);
    }
}

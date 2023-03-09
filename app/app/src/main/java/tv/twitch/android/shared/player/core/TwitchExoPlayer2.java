package tv.twitch.android.shared.player.core;

import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class TwitchExoPlayer2 implements TwitchPlayer {
    private SimpleExoPlayer exoPlayer;

    /* ... */

    @Override
    public void trySetPlaybackSpeed(float speed) { // TODO: __INJECT_METHOD
        exoPlayer.setPlaybackParameters(new PlaybackParameters(speed));
    }
}
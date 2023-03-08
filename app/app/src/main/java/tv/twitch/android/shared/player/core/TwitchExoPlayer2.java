package tv.twitch.android.shared.player.core;

import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;

import tv.orange.core.CoreHook;

public class TwitchExoPlayer2 implements TwitchPlayer {
    private SimpleExoPlayer exoPlayer;

    private final void initPlayer() {
        /* ... */

        CoreHook.Companion.test(exoPlayer); // TODO: __INJECT_CODE
    }

    @Override
    public void trySetPlaybackSpeed(float speed) { // TODO: __INJECT_METHOD
        exoPlayer.setPlaybackParameters(new PlaybackParameters(speed));
    }
}
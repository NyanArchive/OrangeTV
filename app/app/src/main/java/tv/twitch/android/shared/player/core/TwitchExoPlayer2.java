package tv.twitch.android.shared.player.core;

import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;

import tv.orange.core.CoreHook;

public class TwitchExoPlayer2 {
    private SimpleExoPlayer exoPlayer;

    private final void initPlayer() {
        /* ... */

        CoreHook.Companion.test(exoPlayer); // TODO: __INJECT_CODE
    }
}
package tv.twitch.android.shared.player.core;

public class CorePlayer implements TwitchPlayer {
    private TrackingMediaPlayer player;

    /* ... */

    @Override
    public void trySetPlaybackSpeed(float speed) {  // TODO: __INJECT_METHOD
        player.setPlaybackRate(speed);
    }
}

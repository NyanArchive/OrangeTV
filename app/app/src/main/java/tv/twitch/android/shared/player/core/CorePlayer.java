package tv.twitch.android.shared.player.core;

import tv.orange.core.Core;

public class CorePlayer implements TwitchPlayer{
    @Override
    public void trySetPlaybackSpeed(float speed) {  // TODO: __INJECT_METHOD
        Core.showToast("<DEBUG>");
    }
}

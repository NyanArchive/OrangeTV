package com.google.android.exoplayer2;

import androidx.annotation.Nullable;

import tv.orange.models.exception.VirtualImpl;

public class SimpleExoPlayer {
    private final ExoPlayerImpl player = null;

    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) { // TODO: __INJECT_METHOD
        verifyApplicationThread();
        player.setPlaybackParameters(playbackParameters);
    }

    private void verifyApplicationThread() {
        throw new VirtualImpl();
    }
}
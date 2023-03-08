package com.google.android.exoplayer2;

import com.google.android.exoplayer2.util.HandlerWrapper;

public class ExoPlayerImplInternal {
    private final HandlerWrapper handler = null;

    /* ... */

    public void setPlaybackParameters(PlaybackParameters playbackParameters) { // TODO: __INJECT_METHOD
        handler.obtainMessage(4, playbackParameters).sendToTarget();
    }

    /* ... */
}

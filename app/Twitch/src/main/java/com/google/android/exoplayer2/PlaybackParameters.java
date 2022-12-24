package com.google.android.exoplayer2;

import tv.orange.models.exception.VirtualImpl;

public class PlaybackParameters {
    public static final PlaybackParameters DEFAULT = new PlaybackParameters(1.0f);
    public float pitch;
    private int scaledUsPerMs;
    public boolean skipSilence;
    public float speed;

    /* ... */

    public PlaybackParameters(float speed) {
        throw new VirtualImpl();
    }

    /* ... */
}
package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.PlaybackParameters;

import tv.orange.core.CoreHook;

public class StandaloneMediaClock {
    /* ... */

    private PlaybackParameters playbackParameters = CoreHook.hookVodPlayerMediaClock(PlaybackParameters.DEFAULT); // TODO: __INJECT_CODE

    /* ... */
}


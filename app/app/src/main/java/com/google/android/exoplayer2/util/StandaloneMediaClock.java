package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.PlaybackParameters;

import tv.orange.core.Core;

public class StandaloneMediaClock {
    /* ... */

    private PlaybackParameters playbackParameters = Core.hookVodPlayerMediaClock(PlaybackParameters.DEFAULT); // TODO: __INJECT_CODE

    /* ... */
}


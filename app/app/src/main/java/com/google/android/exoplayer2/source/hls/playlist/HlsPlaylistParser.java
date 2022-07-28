package com.google.android.exoplayer2.source.hls.playlist;

import androidx.annotation.Nullable;

import java.io.IOException;

import tv.orange.models.VirtualImpl;

public class HlsPlaylistParser {
    /* ... */

    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";

    /* ... */

    private static final String TAG_PREFETCH = "#EXT-X-TWITCH-PREFETCH"; // TODO: __INJECT_FIELD

    /* ... */

    private static Object parseMediaPlaylist(
            Object multivariantPlaylist,
            @Nullable Object previousMediaPlaylist,
            Object iterator,
            String baseUri)
            throws IOException {
        /* ... */

        long targetDurationUs = 0;

        String line = "";
        long segmentDurationUs = 0;

        if (line.startsWith(TAG_TARGET_DURATION)) { // TODO: __INJECT_CODE
            targetDurationUs = 2 * 1000000L;
        } else if (!line.startsWith("#") || line.startsWith(TAG_PREFETCH)) { // TODO: __INJECT_CODE
            if (line.startsWith(TAG_PREFETCH)) { // TODO: __INJECT_CODE
                segmentDurationUs = targetDurationUs;
                line = line.substring(line.indexOf(':') + 1);
            }
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

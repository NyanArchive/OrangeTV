package com.google.android.exoplayer2.upstream;

import tv.orange.features.proxy.Proxy;

public class DefaultHttpDataSource {
    /* ... */

    public long open(DataSpec dataSpec) {
        dataSpec = Proxy.patchExoPlayerDataSpec(dataSpec); // TODO: __INJECT_CODE

        /* ... */

        return 0;
    }

    /* ... */
}

package com.google.android.exoplayer2.upstream;

import android.net.Uri;

import java.util.Map;

import tv.orange.models.exception.VirtualImpl;

public class DataSpec {
    public long absoluteStreamPosition;
    public int flags;
    public byte[] httpBody;
    public int httpMethod;
    public Map<String, String> httpRequestHeaders;
    public String key;
    public long length;
    public long position;
    public Uri uri;

    /* ... */

    public DataSpec(Uri uri, int httpMethod, byte[] httpBody, long absoluteStreamPosition, long position, long length, String key, int flags, Map<String, String> httpRequestHeaders) {
        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}


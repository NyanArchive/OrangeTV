package tv.twitch.android.models.manifest;

import android.text.TextUtils;

import tv.orange.core.CoreHook;
import tv.orange.models.exception.VirtualImpl;

public class ManifestModel {
    private extm3u mManifest;

    /* ... */

    public String getVideoSessionId() { // TODO: __REPLACE_METHOD
        return CoreHook.fixVideoSessionId(this.mManifest.VideoSessionId);
    }

    public String getProxyServer() { // TODO: __INJECT_METHOD
        String proxyServer = this.mManifest.ProxyServer;
        if (TextUtils.isEmpty(proxyServer)) {
            return null;
        }

        return proxyServer;
    }

    public String getProxyUrl() { // TODO: __INJECT_METHOD
        String url = this.mManifest.ProxyUrl;
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return url;
    }

    public String getManifestUrlWithParams(boolean var1, boolean var2, boolean var3) {
        if (!TextUtils.isEmpty(this.mManifest.ProxyUrl)) { // TODO: __INJECT_CODE
            return this.mManifest.ProxyUrl;
        }

        throw new VirtualImpl();
    }

    /* ... */
}

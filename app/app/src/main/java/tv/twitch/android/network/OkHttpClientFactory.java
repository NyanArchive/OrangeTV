package tv.twitch.android.network;

import java.util.List;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import tv.orange.features.proxy.Proxy;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.network.graphql.cronet.CronetInterceptor;
import tv.twitch.android.network.retrofit.CookieInterceptor;

public class OkHttpClientFactory {
    /* ... */

    private OkHttpClient buildOkHttpClient(long var1, CookieInterceptor var3, List<? extends Interceptor> var4, List<? extends Interceptor> var5, SSLSocketFactory var6, X509TrustManager var7, boolean var8, CronetInterceptor var9) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        /* ... */

        Proxy.maybeAddInterceptor(builder); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

package tv.twitch.android.shared.player.network.manifest;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tv.orange.core.CoreHook;
import tv.orange.features.proxy.Proxy;
import tv.orange.features.vodhunter.Vodhunter;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.AccessTokenResponse;
import tv.twitch.android.models.manifest.extm3u;
import tv.twitch.android.models.player.ManifestProperties;
import tv.twitch.android.network.retrofit.TwitchResponse;
import tv.twitch.android.shared.manifest.fetcher.pub.ManifestResponse;

public class ManifestApi {
    private ManifestService manifestService;

    private interface ManifestService {
        @Headers({"Accept: application/vnd.apple.mpegurl, application/json"})
        @GET("vod/{vodName}.m3u8")
        Single<Response<String>> getVodManifest(@Path("vodName") String str, @Query("nauth") String str2, @Query("nauthsig") String str3, @Query("player_backend") String str4, @Query("cache_buster") long j, @Query("allow_source") boolean z, @Query("allow_audio_only") boolean z2, @Query("cdm") String str5, @Query("acmb") String str6);
    }

    public Single<ManifestResponse> getVodManifest(String vodName, AccessTokenResponse accessTokenResponse, String accessToken, String sig, ManifestProperties manifestProperties) { // TODO: __REPLACE_METHOD
        return toManifestResponse(Vodhunter.get().maybeHookVodManifestResponse(this.manifestService.getVodManifest(vodName, accessToken, sig, manifestProperties.getPlayerImplementation().name(), System.currentTimeMillis(), manifestProperties.getIncludeSourceQuality(), true, manifestProperties.getCdmValue(), manifestProperties.getAdsEncodedClientMetadata()), vodName), accessTokenResponse);
    }

    private final Single<ManifestResponse> toManifestResponse(Single<Response<String>> single, final AccessTokenResponse accessTokenResponse) {
        throw new VirtualImpl();
    }

    public Single<ManifestResponse> getStreamManifest(final String streamName, AccessTokenResponse accessTokenResponse, String accessToken, String sig, boolean z, ManifestProperties manifestProperties) {
        boolean fastBread = CoreHook.getFastBread(); // TODO: __INJECT_CODE

        Single<Response<String>> orgStreamManifest = null;

        orgStreamManifest = Proxy.tryHookStreamManifestResponse(orgStreamManifest, streamName, accessTokenResponse); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public static final ManifestResponse toManifestResponse$lambda(ManifestApi this$0, AccessTokenResponse accessTokenResponse, TwitchResponse response) {
        TwitchResponse.Success success = (TwitchResponse.Success) response;
        extm3u model = null;

        /* ... */

        // success.getRequestUrl()
        String url = Proxy.hookPlaylistUrl(success.getRequestUrl(), model); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }
}

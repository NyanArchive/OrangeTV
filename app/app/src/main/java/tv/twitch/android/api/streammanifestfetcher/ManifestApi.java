package tv.twitch.android.api.streammanifestfetcher;

import io.reactivex.Single;
import tv.orange.core.CoreHook;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.AccessTokenResponse;
import tv.twitch.android.models.player.ManifestProperties;
import tv.twitch.android.shared.manifest.fetcher.pub.ManifestResponse;

public class ManifestApi {
    public Single<ManifestResponse> getStreamManifest(final String streamName, AccessTokenResponse accessTokenResponse, String accessToken, String sig, boolean z, ManifestProperties manifestProperties) {
        boolean fastBread = CoreHook.getFastBread(); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }
}

package tv.twitch.android.shared.player.presenters;

import android.content.Context;

import tv.orange.core.Core;
import tv.orange.core.CoreHook;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.provider.experiments.ExperimentHelper;
import tv.twitch.android.shared.manifest.fetcher.VodManifestProvider;
import tv.twitch.android.shared.manifest.fetcher.model.ManifestPropertiesFactory;
import tv.twitch.android.shared.player.TwitchPlayerProvider;
import tv.twitch.android.shared.player.audiofocus.AudioFocusPresenter;
import tv.twitch.android.shared.player.availability.PlayerAvailabilityTracker;
import tv.twitch.android.shared.player.fetchers.VodFetcher;
import tv.twitch.android.shared.player.trackers.PlayerPresenterTracker;
import tv.twitch.android.shared.referrer.ReferrerPropertiesProvider;
import tv.twitch.android.shared.viewer.pub.IResumeWatchingFetcher;

public class VodPlayerPresenter {
    /* ... */

    public VodPlayerPresenter(Context var1, PlayerPresenterTracker var2, TwitchPlayerProvider var3, VodManifestProvider var4, VodFetcher var5, IResumeWatchingFetcher var6, ExperimentHelper var7, PlayerAvailabilityTracker var8, ManifestPropertiesFactory var9, AudioFocusPresenter var10, ReferrerPropertiesProvider var11) {
        /* ... */

        CoreHook.maybeForceExoPlayerForVods(var3);

        // this.twitchPlayer = var3.getTwitchPlayer(this, var12);

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
package tv.twitch.android.shared.player.overlay.seekable;

import android.view.ViewGroup;

import tv.orange.models.VirtualImpl;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.clips.ClipModel;
import tv.twitch.android.models.videos.VodModel;
import tv.twitch.android.shared.player.overlay.PlayerOverlayPresenter;

public class SeekableOverlayPresenter {
    private SeekbarOverlayPresenter seekbarOverlayPresenter;
    private PlayerOverlayPresenter playerOverlayPresenter;

    /* ... */

    public final void inflateViewDelegate(ViewGroup p0){
        /* ... */

        playerOverlayPresenter.hideChaptersButton(); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public final void bindClip(ClipModel p0, ChannelModel p1){
        playerOverlayPresenter.hideChaptersButton(); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public final void bindVod(VodModel vodModel) {
        playerOverlayPresenter.onBindVodModel(vodModel, seekbarOverlayPresenter); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }
}

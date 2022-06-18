package tv.twitch.android.shared.player.overlay;

import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.models.streams.StreamModel;

public class PlayerOverlayPresenter {
    private PlayerOverlayViewDelegate viewDelegate;

    public final void bindStream(StreamModel streamModel) {
        this.viewDelegate.bindStreamUptime(streamModel); // TODO: __INJECT_CODE
    }

    public final void bindHostedStream(ChannelModel channelModel, StreamModel streamModel) {
        this.viewDelegate.bindStreamUptime(streamModel); // TODO: __INJECT_CODE
    }
}

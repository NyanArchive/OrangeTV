package tv.twitch.android.feature.theatre.metadata;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.core.ViewExtensionsKt;
import tv.twitch.android.models.channel.ChannelModel;
import tv.twitch.android.shared.subscriptions.ExtendedSubButtonState;

public class ExtendedPlayerMetadataViewDelegate {
    private View followButtonExtended;
    /* ... */

    public final ExtendedPlayerMetadataViewDelegate create(Context context, ViewGroup viewGroup, ViewGroup viewGroup2, ViewGroup viewGroup3) {
        // View inflate = LayoutInflater.from(context).inflate(R$layout.player_metadata_view_extended, viewGroup, false);

        int id = UI.hookPlayerMetadataViewId(0); // TODO: __HOOK_ARG

        throw new VirtualImpl();
    }

    private final void showExtendedButtons(ExtendedSubButtonState extendedSubButtonState, ChannelModel channelModel) {
        /* ... */

        ViewExtensionsKt.visibilityForBoolean(this.followButtonExtended, UI.getShowFollowButtonExtended()); // TODO: __REPLACE_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}

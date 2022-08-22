package tv.twitch.android.feature.theatre.metadata;

import android.content.Context;
import android.view.ViewGroup;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class ExtendedPlayerMetadataViewDelegate {
    /* ... */

    public final ExtendedPlayerMetadataViewDelegate create(Context context, ViewGroup viewGroup, ViewGroup viewGroup2, ViewGroup viewGroup3) {
        // View inflate = LayoutInflater.from(context).inflate(R$layout.player_metadata_view_extended, viewGroup, false);

        int id = UI.hookPlayerMetadataViewId(0); // TODO: __HOOK_ARG

        throw new VirtualImpl();
    }

    /* ... */
}

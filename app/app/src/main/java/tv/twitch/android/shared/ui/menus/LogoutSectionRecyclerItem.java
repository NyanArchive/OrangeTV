package tv.twitch.android.shared.ui.menus;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tv.orange.features.ui.UI;
import tv.orange.features.ui.bridge.ISectionFooterRecyclerItemViewHolder;
import tv.orange.models.exception.VirtualImpl;

public class LogoutSectionRecyclerItem {
    /* ... */

    public void bindToViewHolder(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof SectionFooterRecyclerItemViewHolder) {
            /* ... */

            UI.get().updateLogoutSectionRecyclerItem(viewHolder); // TODO: __INJECT_CODE
        }

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    public static final class SectionFooterRecyclerItemViewHolder extends RecyclerView.ViewHolder implements ISectionFooterRecyclerItemViewHolder {
        private final TextView orangetvVersionTextView; // TODO: __INJECT_FIELD
        private final TextView orangetvBuildDateTextView; // TODO: __INJECT_FIELD

        /* ... */

        public SectionFooterRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            /* ... */

            orangetvVersionTextView = UI.get().getOrangeTvBuildTV(itemView); // TODO: __INJECT_CODE
            orangetvBuildDateTextView = UI.get().getOrangeTvBuildDateTV(itemView); // TODO: __INJECT_CODE

            throw new VirtualImpl();
        }

        @NonNull
        @Override
        public TextView getOrangeTVSection() { // TODO: __INJECT_METHOD
            return orangetvVersionTextView;
        }

        @NonNull
        @Override
        public TextView getOrangeTVDateSection() { // TODO: __INJECT_METHOD
            return orangetvBuildDateTextView;
        }

        /* ... */
    }
}

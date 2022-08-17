package tv.twitch.android.shared.ui.menus;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tv.orange.features.ui.UI;
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

    public static final class SectionFooterRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        /* ... */

        public SectionFooterRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            /* ... */

            throw new VirtualImpl();
        }

        /* ... */
    }
}

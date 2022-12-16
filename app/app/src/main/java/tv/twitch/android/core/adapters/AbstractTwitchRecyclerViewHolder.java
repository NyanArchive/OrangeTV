package tv.twitch.android.core.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import tv.orange.features.chat.bridge.VHBinder;
import tv.orange.models.exception.VirtualImpl;

public abstract class AbstractTwitchRecyclerViewHolder extends RecyclerView.ViewHolder {
    /* ... */

    public AbstractTwitchRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        throw new VirtualImpl();
    }

    /* ... */

    public void onBindDataItem(RecyclerAdapterItem item) {
        /* ... */

        VHBinder.INSTANCE.onBindToViewHolder(this, item); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    public void onRecycled() {
        throw new VirtualImpl();
    }

    /* ... */
}

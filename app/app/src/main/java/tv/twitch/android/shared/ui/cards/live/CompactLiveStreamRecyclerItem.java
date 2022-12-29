package tv.twitch.android.shared.ui.cards.live;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.CompositeDisposable;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.adapters.AbstractTwitchRecyclerViewHolder;
import tv.twitch.android.core.adapters.ChannelIdProvider;

public class CompactLiveStreamRecyclerItem implements ChannelIdProvider {
    /* ... */

    @Override
    public int getChannelId() {
        throw new VirtualImpl();
    }

    /* ... */

    public static final class CompactLiveStreamItemViewHolder extends AbstractTwitchRecyclerViewHolder {
        private final CompositeDisposable disposables = new CompositeDisposable(); // TODO: __INJECT_FIELD
        private final TextView uptime; // TODO: __INJECT_FIELD


        public CompactLiveStreamItemViewHolder(@NonNull View itemView) {
            super(itemView);

            uptime = UI.getUptimeButton((RecyclerView.ViewHolder) this); // TODO: __INJECT_CODE
        }

        public TextView getUptime() { // TODO: __INJECT_METHOD
            return uptime;
        }

        public void tryBindUptime(int channelId) { // TODO: __INJECT_METHOD
            disposables.add(UI.get().tryBindUptime(getUptime(), channelId));
        }

        @Override
        public void onRecycled() { // TODO: __INJECT_METHOD
            super.onRecycled();
            disposables.clear();
        }
    }

    /* ... */

    public void bindToViewHolder(CompactLiveStreamItemViewHolder viewHolder) {
        viewHolder.tryBindUptime(getChannelId()); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
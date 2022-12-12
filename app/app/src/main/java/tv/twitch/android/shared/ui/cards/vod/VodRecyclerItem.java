package tv.twitch.android.shared.ui.cards.vod;

import tv.orange.features.vodhunter.Vodhunter;
import tv.twitch.android.models.videos.VodModel;

public class VodRecyclerItem {
    public static final class VodVideoCardViewHolder {
        public void onBindVideoDataItem(VodRecyclerItem item) {
            /* ... */

            VodModel model = null;

            if (model.isRestricted() && !Vodhunter.isEnabled()) { // TODO: __INJECT_CODE
                /* ... */
            }

            /* ... */
        }
    }
}

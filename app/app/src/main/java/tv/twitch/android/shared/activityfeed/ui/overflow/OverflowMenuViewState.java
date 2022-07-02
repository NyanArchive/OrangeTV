package tv.twitch.android.shared.activityfeed.ui.overflow;

import tv.orange.features.stv.AvatarsHookProvider;
import tv.twitch.android.models.channel.ChannelInfo;

public abstract class OverflowMenuViewState {
    /* ... */

    public static final class OverflowMenuShownState extends OverflowMenuViewState {
        private final String avatarUrl = null;
        private final ChannelInfo channelInfo = null;

        /* ... */

        public final String getAvatarUrl() { // TODO: __REPLACE_METHOD
            return AvatarsHookProvider.get().hookProfileImageUrl(avatarUrl, channelInfo.getName());
        }
    }

    /* ... */
}
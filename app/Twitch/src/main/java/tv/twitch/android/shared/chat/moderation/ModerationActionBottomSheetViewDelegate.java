package tv.twitch.android.shared.chat.moderation;

import kotlin.jvm.internal.DefaultConstructorMarker;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;

public class ModerationActionBottomSheetViewDelegate {
    /* ... */

    public static abstract class ModerationActionButtonEvent implements ViewDelegateEvent {
        public ModerationActionButtonEvent(DefaultConstructorMarker defaultConstructorMarker) {
        }

        /* ... */

        public static class ViewLogs extends ModerationActionButtonEvent { // TODO: __INJECT_CLASS
            private final String userId;
            private final String channelId;

            public ViewLogs(String userId, String channelId) {
                super(null);
                this.userId = userId;
                this.channelId = channelId;
            }

            public String getChannelId() {
                return channelId;
            }

            public String getUserId() {
                return userId;
            }
        }

        /* ... */
    }

    /* ... */
}

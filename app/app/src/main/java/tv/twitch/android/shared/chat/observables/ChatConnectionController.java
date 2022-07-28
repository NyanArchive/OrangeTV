package tv.twitch.android.shared.chat.observables;

import tv.orange.core.Core;
import tv.orange.models.VirtualImpl;
import tv.twitch.ErrorCode;
import tv.twitch.android.sdk.ChannelState;

public class ChatConnectionController {
    /* ... */

    public final void handleChannelStateChange(int i, ChannelState channelState, ErrorCode errorCode) {
        if (i == 1) {
            /* ... */

            Core.get().onConnectingToChannel(i); // TODO: __INJECT_CODE
        }
        if (i == 2) {
            /* ... */

            Core.get().onConnectedToChannel(i); // TODO: __INJECT_CODE
        }

        throw new VirtualImpl();
    }

    /* ... */
}
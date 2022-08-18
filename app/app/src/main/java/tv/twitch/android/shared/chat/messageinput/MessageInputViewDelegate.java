package tv.twitch.android.shared.chat.messageinput;

import android.view.View;

import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;

public class MessageInputViewDelegate extends BaseViewDelegate {
    public MessageInputViewDelegate(View view) {
        super(view);

        throw new VirtualImpl();
    }

    /* ... */

    public final void render() {
        /* ... */

        boolean z2 = false;
        z2 = ChatHookProvider.changeBitsButtonVisibility(z2);  // TODO: __HOOK_PARAM

        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void show() { // TODO: __INJECT_METHOD
        if (UI.shouldHideMessageInput(getContext())) {
            super.hide();
        } else {
            super.show();
        }
    }

    /* ... */
}

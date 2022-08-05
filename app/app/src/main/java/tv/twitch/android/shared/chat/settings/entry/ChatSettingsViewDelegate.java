package tv.twitch.android.shared.chat.settings.entry;

import android.content.Context;
import android.view.View;

import java.util.List;

import io.reactivex.Flowable;
import tv.orange.features.chat.bridge.ChatSettingsOrangeViewDelegate;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.RxViewDelegate;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;

public class ChatSettingsViewDelegate extends RxViewDelegate {
    private final ChatSettingsOrangeViewDelegate orangeViewDelegate; // TODO: __INJECT_FIELD

    /* ... */

    public ChatSettingsViewDelegate(Context context, View root) {
        super(null);
        /* ... */

        orangeViewDelegate = new ChatSettingsOrangeViewDelegate(context, this.getContentView()); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void render(ViewDelegateState viewDelegateState) {
        /* ... */

        orangeViewDelegate.render(viewDelegateState); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public static abstract class ChatSettingsEvents implements ViewDelegateEvent {/* ... */
    }

    @Override
    public Flowable<ChatSettingsEvents> eventObserver() {
        /* ... */

        List listOf = null;

        listOf = orangeViewDelegate.injectEvents(listOf); // TODO: __INJECT_CODE

        return Flowable.merge(listOf);
    }

}

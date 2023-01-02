package tv.twitch.android.shared.share.panel;

import android.content.Context;
import android.view.View;

import kotlin.jvm.internal.DefaultConstructorMarker;
import tv.orange.features.vodhunter.Vodhunter;
import tv.orange.features.vodhunter.bridge.ISharePanelWidgetViewDelegate;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.RxViewDelegate;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.shared.ui.elements.bottomsheet.InteractiveRowView;

public class SharePanelWidgetViewDelegate extends RxViewDelegate implements ISharePanelWidgetViewDelegate { // TODO: __IMPLEMENT
    private final InteractiveRowView downloadClipButton; // TODO: __INJECT_FIELD

    public SharePanelWidgetViewDelegate(Context context, View view) {
        super(view);

        /* ... */

        downloadClipButton = Vodhunter.get().getDownloadClipButton(this); // TODO: __INJECT_CODE
    }

    @Override
    public void pushDownloadClipEvent() { // TODO: __INJECT_METHOD
        pushEvent(Event.DownloadClicked.INSTANCE);
    }

    @Override
    public void render(ViewDelegateState state) {
        renderDownloadButton(state); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    private void renderDownloadButton(ViewDelegateState state) { // TODO: __INJECT_METHOD
        if (downloadClipButton != null) {
            downloadClipButton.setVisibility(state instanceof State.InitializeForClipOrChomment ? View.VISIBLE : View.GONE);
        }
    }

    /* ... */

    public static abstract class Event implements ViewDelegateEvent {
        public Event(DefaultConstructorMarker defaultConstructorMarker) {
            throw new VirtualImpl();
        }

        private Event() {
            throw new VirtualImpl();
        }

        public static final class DownloadClicked extends Event { // TODO: __INJECT_CLASS
            public static final DownloadClicked INSTANCE = new DownloadClicked();

            private DownloadClicked() { // TODO: __PATCH_SUPER
                super(null);
            }
        }

        /* ... */
    }

    public static abstract class State implements ViewDelegateState {
        /* ... */

        public static final class InitializeForClipOrChomment extends State {
            /* ... */
        }

        /* ... */
    }

    /* ... */
}
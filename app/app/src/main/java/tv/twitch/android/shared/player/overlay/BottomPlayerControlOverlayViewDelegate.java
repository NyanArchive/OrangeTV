package tv.twitch.android.shared.player.overlay;

import android.view.View;
import android.widget.ImageView;

import tv.orange.features.refreshstream.Hook;
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate;

import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;

public class BottomPlayerControlOverlayViewDelegate extends BaseViewDelegate implements IBottomPlayerControlOverlayViewDelegate { // TODO: @features:refreshstream
    private BottomPlayerControlListener mBottomPlayerControlListener;

    private ImageView mRefreshStreamButton; // TODO: __INJECT_FIELD


    public BottomPlayerControlOverlayViewDelegate(View view) {
        super(view);

        /* ... */

        mRefreshStreamButton = Hook.Companion.getInstance().getRefreshStreamButton(this); // TODO: __INJECT_CODE
    }

    @Override
    public void onRefreshStreamClicked() { // TODO: __INJECT_METHOD
        mBottomPlayerControlListener.onRefreshClicked();
    }

    public interface BottomPlayerControlListener {
        void onChangeOrientationButtonClicked();

        void onChatModeToggleClicked();

        void onExpandVideoButtonClicked();

        void onShowChatButtonClicked();

        void onShowChatInputClicked();

        void onShowChatSettingsClicked();

        void onVideoDebugInfoButtonClicked();

        void onViewCountClicked();

        void onRefreshClicked(); // TODO: __INJECT_METHOD
    }
}

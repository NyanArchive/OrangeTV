package tv.twitch.android.shared.player.overlay;

import android.view.View;
import android.widget.ImageView;

import tv.orange.features.refreshstream.Hook;
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate;
import tv.orange.features.streamuptime.StreamUptimeHookProvider;
import tv.orange.features.streamuptime.bridge.StreamUptimeView;
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;
import tv.twitch.android.models.streams.StreamModel;

public class BottomPlayerControlOverlayViewDelegate extends BaseViewDelegate implements IBottomPlayerControlOverlayViewDelegate { // TODO: @features:refreshstream
    private BottomPlayerControlListener mBottomPlayerControlListener;

    private ImageView mRefreshStreamButton; // TODO: __INJECT_FIELD
    private StreamUptimeView mLiveIndicatorLeftText; // TODO: __REPLACE_TYPE_FIELD


    public BottomPlayerControlOverlayViewDelegate(View view) {
        super(view);

        /* ... */

        mRefreshStreamButton = Hook.get().getRefreshStreamButton(this); // TODO: __INJECT_CODE
    }

    public void bindStreamUptime(StreamModel model) { // TODO: __INJECT_METHOD
        StreamUptimeHookProvider.get().bindStreamUptime(mLiveIndicatorLeftText, model);
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

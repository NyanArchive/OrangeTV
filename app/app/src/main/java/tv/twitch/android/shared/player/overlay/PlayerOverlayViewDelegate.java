package tv.twitch.android.shared.player.overlay;

import android.view.View;
import android.view.ViewGroup;

import io.reactivex.subjects.PublishSubject;
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;
import tv.twitch.android.models.player.PlayerMode;
import tv.twitch.android.models.streams.StreamModel;
import tv.twitch.android.shared.ads.pub.TheatreAdsState;

public class PlayerOverlayViewDelegate extends BaseViewDelegate implements IPlayerOverlay { // TODO: @features:refreshstream
    public PlayerOverlayViewDelegate(View view) {
        super(view);

        Object r2 = new BottomPlayerControlOverlayViewDelegate.BottomPlayerControlListener() {

            @Override
            public void onChangeOrientationButtonClicked() {
                /* ... */
            }

            @Override
            public void onChatModeToggleClicked() {
                /* ... */
            }

            @Override
            public void onExpandVideoButtonClicked() {
                /* ... */
            }

            @Override
            public void onShowChatButtonClicked() {
                /* ... */
            }

            @Override
            public void onShowChatInputClicked() {
                /* ... */
            }

            @Override
            public void onShowChatSettingsClicked() {

            }

            @Override
            public void onVideoDebugInfoButtonClicked() {
                /* ... */
            }

            @Override
            public void onViewCountClicked() {
                /* ... */
            }

            @Override
            public void onRefreshClicked() { // TODO: __INJECT_METHOD
                PlayerOverlayViewDelegate.this.getOverlayLayoutController().hideOverlay();
                PlayerOverlayViewDelegate.this.getPlayerOverlayEventsSubject().onNext(PlayerOverlayEvents.Refresh.INSTANCE); // FIXME: wtf?
            }
        };
    }

    @Override
    public void layoutOverlayForState(boolean b, PlayerMode playerMode, ViewGroup viewGroup, ViewGroup viewGroup1, boolean b1, TheatreAdsState theatreAdsState) {
        /* ... */
    }

    @Override
    public void setChatButtonState(boolean b) {
        /* ... */
    }

    @Override
    public void setChatButtonVisible(boolean b) {
        /* ... */
    }

    @Override
    public void setChatModeToggleVisible(boolean b) {
        /* ... */
    }

    @Override
    public void setChatSettingsButtonVisible(boolean b) {
        /* ... */
    }

    @Override
    public void setWriteChatButtonVisible(boolean b) {
        /* ... */
    }

    public final OverlayLayoutController getOverlayLayoutController() {
        return null;
    }

    public final PublishSubject<PlayerOverlayEvents> getPlayerOverlayEventsSubject() {
        return null;
    }

    public final void bindStreamUptime(StreamModel model) { // TODO: __INJECT_METHOD
        getBottomPlayerControlOverlayViewDelegate().bindStreamUptime(model);
    }

    private BottomPlayerControlOverlayViewDelegate getBottomPlayerControlOverlayViewDelegate() {
        return null;
    }
}

package tv.twitch.android.shared.player.overlay;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import io.reactivex.subjects.PublishSubject;
import tv.orange.features.chapters.ChaptersHook;
import tv.orange.features.chapters.bridge.IChaptersDelegate;
import tv.orange.features.timer.Hook;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;
import tv.twitch.android.models.player.PlayerMode;
import tv.twitch.android.models.videos.VodModel;
import tv.twitch.android.shared.ads.pub.TheatreAdsState;
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter;

public class PlayerOverlayViewDelegate extends BaseViewDelegate implements IPlayerOverlay, IChaptersDelegate { // TODO: __IMPLEMENT
    private final ImageView orangeTimerButton; // TODO: __INJECT_FIELD
    private final ImageView chaptersButton; // TODO: __INJECT_FIELD

    /* ... */

    public PlayerOverlayViewDelegate(View view) {
        super(view);

        /* ... */

        orangeTimerButton = Hook.get().getTimerButton(this); // TODO: __INJECT_CODE
        chaptersButton = ChaptersHook.get().getChaptersButton(this); // TODO: __INJECT_CODE

        /* ... */

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

        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void layoutOverlayForState(boolean b, PlayerMode playerMode, ViewGroup viewGroup, ViewGroup viewGroup1, boolean b1, TheatreAdsState theatreAdsState) {
        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void setChatButtonState(boolean b) {
        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void setChatButtonVisible(boolean b) {
        /* ... */

        throw new VirtualImpl();
    }

    public final OverlayLayoutController getOverlayLayoutController() {
        /* ... */

        throw new VirtualImpl();
    }

    public final PublishSubject<PlayerOverlayEvents> getPlayerOverlayEventsSubject() {
        /* ... */

        throw new VirtualImpl();
    }

    private BottomPlayerControlOverlayViewDelegate getBottomPlayerControlOverlayViewDelegate() {
        /* ... */

        throw new VirtualImpl();
    }

    @Override
    public void hideChaptersButton() { // TODO: __INJECT_METHOD
        ChaptersHook.get().hideChaptersButton(chaptersButton);
    }

    @Override
    public void onBindVodModel(@NonNull VodModel vod, @NonNull SeekbarOverlayPresenter presenter) { // TODO: __INJECT_METHOD
        ChaptersHook.get().bindChaptersButton(chaptersButton, vod, presenter);
    }
}

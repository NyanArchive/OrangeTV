package tv.orange.features.refreshstream.view

import android.widget.ImageView
import tv.orange.core.util.ViewUtil.getView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView {
        return delegate.contentView.getView("bottom_player_control_overlay_widget__refresh_button")
    }
}
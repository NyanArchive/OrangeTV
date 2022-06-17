package tv.orange.features.refreshstream.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate

class ViewFactoryImpl : ViewFactory {
    override fun createRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView? {
        val id = delegate.context.resources.getIdentifier(
            "bottom_player_control_overlay_widget__refresh_button",
            "id",
            delegate.context.packageName
        )
        if (id == 0) {
            return null
        }

        return delegate.contentView.findViewById(id)
    }
}
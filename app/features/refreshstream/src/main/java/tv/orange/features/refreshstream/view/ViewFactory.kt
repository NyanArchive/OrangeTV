package tv.orange.features.refreshstream.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate

interface ViewFactory {
    fun createRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView
}
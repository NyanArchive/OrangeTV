package tv.orange.features.vodspeed.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate

interface ViewFactory {
    fun createVodSpeedButton(delegate: PlayerOverlayViewDelegate): ImageView
}
package tv.orange.features.timer.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate

interface ViewFactory {
    fun createTimerButton(delegate: PlayerOverlayViewDelegate): ImageView
}
package tv.orange.features.chapters.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate

interface ViewFactory {
    fun createChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView?
}
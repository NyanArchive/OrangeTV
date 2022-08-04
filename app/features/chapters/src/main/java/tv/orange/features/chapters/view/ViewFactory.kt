package tv.orange.features.chapters.view

import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import tv.orange.features.chapters.data.view.ChaptersFragment
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate

interface ViewFactory {
    fun createChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView?
    fun getChaptersFragment(): ChaptersFragment
}
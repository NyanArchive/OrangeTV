package tv.orange.features.timer.view

import android.widget.ImageView
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createTimerButton(delegate: PlayerOverlayViewDelegate): ImageView? {
        val id = delegate.context.resources.getIdentifier(
            "orange_timer_button",
            "id",
            delegate.context.packageName
        )
        if (id == 0) {
            return null
        }

        return delegate.contentView.findViewById(id)
    }
}
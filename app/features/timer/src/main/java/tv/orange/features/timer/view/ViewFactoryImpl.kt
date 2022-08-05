package tv.orange.features.timer.view

import android.widget.ImageView
import tv.orange.core.ViewUtil.getView
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createTimerButton(delegate: PlayerOverlayViewDelegate): ImageView {
        return delegate.contentView.getView("orange_timer_button")
    }
}
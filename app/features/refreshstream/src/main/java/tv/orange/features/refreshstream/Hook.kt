package tv.orange.features.refreshstream

import android.widget.ImageView
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate
import tv.orange.features.refreshstream.view.ViewFactory
import tv.orange.features.refreshstream.view.ViewFactoryImpl
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate

class Hook(private val viewFactory: ViewFactory) {
    fun getRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView? {
        return viewFactory.createRefreshStreamButton(delegate)?.apply {
            setOnClickListener {
                delegate.cast<IBottomPlayerControlOverlayViewDelegate>().onRefreshStreamClicked()
            }
        }
    }

    companion object {
        val instance by lazy {
            Hook(ViewFactoryImpl())
        }
    }
}
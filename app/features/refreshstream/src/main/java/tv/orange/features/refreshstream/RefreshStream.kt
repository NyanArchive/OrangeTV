package tv.orange.features.refreshstream

import android.widget.ImageView
import tv.orange.core.Core
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate
import tv.orange.features.refreshstream.di.scope.RefreshStreamScope
import tv.orange.features.refreshstream.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import javax.inject.Inject

@RefreshStreamScope
class RefreshStream @Inject constructor(val viewFactory: ViewFactory) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(RefreshStream::class.java)
    }

    fun getRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView? {
        return viewFactory.createRefreshStreamButton(delegate)?.apply {
            setOnClickListener {
                delegate.cast<IBottomPlayerControlOverlayViewDelegate>().onRefreshStreamClicked()
            }
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
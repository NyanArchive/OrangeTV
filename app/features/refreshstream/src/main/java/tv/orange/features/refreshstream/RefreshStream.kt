package tv.orange.features.refreshstream

import android.widget.ImageView
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate
import tv.orange.features.refreshstream.di.scope.RefreshStreamScope
import tv.orange.features.refreshstream.view.ViewFactory
import tv.orange.models.Feature
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import javax.inject.Inject

@RefreshStreamScope
class RefreshStream @Inject constructor(val viewFactory: ViewFactory) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(RefreshStream::class.java)
    }

    fun getRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView? {
        val view = viewFactory.createRefreshStreamButton(delegate)?.apply {
            setOnClickListener {
                delegate.cast<IBottomPlayerControlOverlayViewDelegate>().onRefreshStreamClicked()
            }
        }
        Logger.debug("view: $view")
        return view
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
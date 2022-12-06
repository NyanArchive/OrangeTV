package tv.orange.features.refreshstream

import android.widget.ImageView
import tv.orange.core.Core
import tv.orange.core.compat.ClassCompat
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate
import tv.orange.features.refreshstream.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import javax.inject.Inject

class RefreshStream @Inject constructor(val viewFactory: ViewFactory) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(RefreshStream::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(RefreshStream::class.java)
        }
    }

    fun getRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView {
        return viewFactory.createRefreshStreamButton(delegate = delegate).apply {
            setOnClickListener {
                ClassCompat.invokeIf<IBottomPlayerControlOverlayViewDelegate>(delegate) {
                    it.onRefreshStreamClicked()
                }
            }
            if (!Flag.SHOW_REFRESH_BUTTON.asBoolean()) {
                changeVisibility(false)
            }
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
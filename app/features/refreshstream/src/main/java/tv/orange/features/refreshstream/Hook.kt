package tv.orange.features.refreshstream

import android.widget.ImageView
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.refreshstream.bridge.IBottomPlayerControlOverlayViewDelegate
import tv.orange.features.refreshstream.di.component.DaggerRefreshStreamComponent
import tv.orange.features.refreshstream.di.scope.RefreshStreamScope
import tv.orange.features.refreshstream.view.ViewFactory
import tv.twitch.android.shared.player.overlay.BottomPlayerControlOverlayViewDelegate
import javax.inject.Inject

@RefreshStreamScope
class Hook @Inject constructor(val viewFactory: ViewFactory) {
    fun getRefreshStreamButton(delegate: BottomPlayerControlOverlayViewDelegate): ImageView? {
        return viewFactory.createRefreshStreamButton(delegate)?.apply {
            setOnClickListener {
                delegate.cast<IBottomPlayerControlOverlayViewDelegate>().onRefreshStreamClicked()
            }
        }
    }

    companion object {
        private val INSTANCE by lazy {
            val instance = DaggerRefreshStreamComponent.builder()
                .coreComponent(Core.getProvider(CoreComponent::class).get())
                .build().hook

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }
    }
}
package tv.orange.features.timer

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.timer.bridge.OrangeTimerFragment
import tv.orange.features.timer.di.component.DaggerTimerComponent
import tv.orange.features.timer.di.scope.TimerScope
import tv.orange.features.timer.view.ViewFactory
import tv.twitch.android.core.fragments.routing.FragmentUtil
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject

@TimerScope
class Hook @Inject constructor(val viewFactory: ViewFactory) {
    companion object {
        private val INSTANCE by lazy {
            val instance = DaggerTimerComponent.builder()
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

    fun getTimerButton(delegate: PlayerOverlayViewDelegate): ImageView? {
        return viewFactory.createTimerButton(delegate)?.apply {
            setOnClickListener {
                Logger.debug("clicked!")
                val fragment = OrangeTimerFragment()
                fragment.show((it.context as FragmentActivity).supportFragmentManager, "test2")
            }
        }
    }

    fun test(activity: FragmentActivity) {
        FragmentUtil.Companion!!.addOrRecreateFragment(
            activity,
            OrangeTimerFragment(),
            "test",
            null
        )
    }
}
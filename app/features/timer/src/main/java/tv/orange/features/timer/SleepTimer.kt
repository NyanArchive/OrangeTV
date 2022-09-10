package tv.orange.features.timer

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.features.timer.bridge.OrangeTimerFragment
import tv.orange.features.timer.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject

class SleepTimer @Inject constructor(
    val viewFactory: ViewFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(SleepTimer::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(SleepTimer::class.java)
        }
    }

    fun getTimerButton(delegate: PlayerOverlayViewDelegate): ImageView {
        return viewFactory.createTimerButton(delegate = delegate).apply {
            setOnClickListener { button ->
                OrangeTimerFragment().show(
                    (button.context as FragmentActivity).supportFragmentManager,
                    "orange_timer"
                )
            }
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
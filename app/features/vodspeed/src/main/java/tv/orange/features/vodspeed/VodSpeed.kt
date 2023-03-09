package tv.orange.features.vodspeed

import android.app.AlertDialog
import android.content.Context
import android.widget.ImageView
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.features.vodspeed.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.player.overlay.PlayerOverlayEvents
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject

class VodSpeed @Inject constructor(
    val viewFactory: ViewFactory,
    val rm: ResourcesManagerCore
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(VodSpeed::class.java)
    }

    fun getVodSpeedButton(delegate: PlayerOverlayViewDelegate): ImageView {
        return viewFactory.createVodSpeedButton(delegate = delegate)
    }

    fun hideVodSpeedButton(button: ImageView) {
        button.changeVisibility(isVisible = false)
    }

    fun bindVodSpeedButton(
        button: ImageView,
        events: PublishSubject<PlayerOverlayEvents>,
        context: Context
    ) {
        button.changeVisibility(isVisible = true)
        button.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(rm.getStringId("orange_vodspeed_change_speed"))

            val animals = arrayOf("0.5", "0.75", "1.0", "1.25", "1.5", "1.75", "2.0", "2.5", "3.0")
            var checkedItem = 2
            builder.setSingleChoiceItems(animals, checkedItem) { _, index ->
                checkedItem = index
            }

            builder.setPositiveButton(android.R.string.ok) { dialog, index ->
                val speed = when (checkedItem) {
                    0 -> 0.5f
                    1 -> 0.75f
                    2 -> 1.0f
                    3 -> 1.25f
                    4 -> 1.5f
                    5 -> 1.75f
                    6 -> 2.0f
                    7 -> 2.5f
                    8 -> 3.0f
                    else -> 1.0f
                }

                events.onNext(PlayerOverlayEvents.ChangePlaybackSpeed(speed))
            }

            builder.create().show()
        }
    }

    override fun onCreateFeature() {}
}
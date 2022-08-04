package tv.orange.features.chapters

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.features.chapters.view.ViewFactory
import tv.orange.models.Feature
import tv.twitch.android.models.videos.VodModel
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter
import javax.inject.Inject

class VodChapters @Inject constructor(
    val viewFactory: ViewFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(VodChapters::class.java)
    }

    fun getChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView? {
        Logger.debug("delegate: $delegate")
        return viewFactory.createChaptersButton(delegate)
    }

    fun hideChaptersButton(button: ImageView) {
        Logger.debug("button: $button")
        button.visibility = View.GONE
    }

    fun bindChaptersButton(
        button: ImageView,
        vod: VodModel,
        presenter: SeekbarOverlayPresenter
    ) {
        Logger.debug("button: $button, vod: $vod, presenter: $presenter")
        button.visibility = View.VISIBLE
        button.setOnClickListener {
            val id = if (vod.id.startsWith("v")) {
                vod.id.substring(1)
            } else {
                vod.id
            }
            Logger.debug("called")
            val sfm = (button.context as FragmentActivity).supportFragmentManager
            val fragment = viewFactory.getChaptersFragment()
            fragment.bindSeekPresenter(presenter)
            fragment.show(sfm, "chapters")
            fragment.load(id)
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
package tv.orange.features.chapters

import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.features.chapters.view.ViewFactory
import tv.orange.models.abc.Feature
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

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(VodChapters::class.java)
        }

        private fun fixVodId(vodId: String): String {
            return if (vodId.startsWith("v")) {
                vodId.substring(1)
            } else {
                vodId
            }
        }
    }

    fun getChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView {
        return viewFactory.createChaptersButton(delegate = delegate)
    }

    fun hideChaptersButton(button: ImageView) {
        button.changeVisibility(isVisible = false)
    }

    fun bindChaptersButton(
        button: ImageView,
        vod: VodModel,
        presenter: SeekbarOverlayPresenter
    ) {
        button.changeVisibility(isVisible = true)
        button.setOnClickListener {
            val fragmentManager = (button.context as FragmentActivity).supportFragmentManager
            val fragment = viewFactory.getChaptersFragment()
            fragment.bindSeekPresenter(presenter)
            fragment.show(fragmentManager, "orange_chapters")
            fragment.load(fixVodId(vod.id))
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
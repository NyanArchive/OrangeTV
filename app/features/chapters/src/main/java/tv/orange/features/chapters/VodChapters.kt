package tv.orange.features.chapters

import android.os.Handler
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.compat.ClassCompat.getPrivateField
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.features.chapters.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.models.videos.VodModel
import tv.twitch.android.shared.player.overlay.PlayerOverlayEvents
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter
import javax.inject.Inject

class VodChapters @Inject constructor(
    val viewFactory: ViewFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(VodChapters::class.java)

        private fun fixVodId(vodId: String): String {
            return if (vodId.startsWith("v")) {
                vodId.substring(1)
            } else {
                vodId
            }
        }
    }

    fun getChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView {
        val h = Handler(delegate.context.mainLooper)
        var z = 1L
        for (i in listOf(2.5f, 1.5f, 0.5f, 0.75f, 1.0f)) {
            h.postDelayed({
                LoggerImpl.devDebug("start")
                Core.showToast("Change speed to $i")
                delegate.getPrivateField<PublishSubject<PlayerOverlayEvents>>("playerOverlayEventsSubject")
                    .onNext(PlayerOverlayEvents.ChangePlaybackSpeed(i))
                LoggerImpl.devDebug("stop")
            }, z++ * 3000)
        }
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

    override fun onCreateFeature() {}
}
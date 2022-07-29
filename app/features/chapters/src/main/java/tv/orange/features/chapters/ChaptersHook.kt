package tv.orange.features.chapters

import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.chapters.di.component.ChaptersComponent
import tv.orange.features.chapters.di.component.DaggerChaptersComponent
import tv.orange.features.chapters.di.scope.ChaptersScope
import tv.orange.features.chapters.view.ViewFactory
import tv.twitch.android.models.videos.VodModel
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter
import javax.inject.Inject

@ChaptersScope
class ChaptersHook @Inject constructor(val viewFactory: ViewFactory) {
    private lateinit var chaptersComponent: ChaptersComponent

    companion object {
        private val INSTANCE by lazy {
            val component = DaggerChaptersComponent.factory().create(
                Core.getProvider(CoreComponent::class).get(),
                Core.getProvider(GraphQlService::class).get()
            )

            val instance = component.hook
            component.hook.chaptersComponent = component

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): ChaptersHook {
            return INSTANCE
        }
    }

    fun getChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView? {
        Logger.debug("called")
        return viewFactory.createChaptersButton(delegate)
    }

    fun hideChaptersButton(chaptersButton: ImageView) {
        Logger.debug("called")
        chaptersButton.visibility = View.GONE
    }

    fun bindChaptersButton(
        chaptersButton: ImageView,
        vod: VodModel,
        presenter: SeekbarOverlayPresenter
    ) {
        chaptersButton.visibility = View.VISIBLE
        chaptersButton.setOnClickListener {
            val id = if (vod.id.startsWith("v")) {
                vod.id.substring(1)
            } else {
                vod.id
            }
            Logger.debug("called")
            val sfm = (chaptersButton.context as FragmentActivity).supportFragmentManager
            val fragment = chaptersComponent.chaptersFragment
            fragment.show(sfm, "chapters")
            fragment.load(id)
        }
    }
}
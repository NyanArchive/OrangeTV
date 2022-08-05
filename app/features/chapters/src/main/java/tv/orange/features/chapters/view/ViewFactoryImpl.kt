package tv.orange.features.chapters.view

import android.widget.ImageView
import tv.orange.core.ViewUtil.getView
import tv.orange.features.chapters.data.view.ChaptersFragment
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject
import javax.inject.Provider

class ViewFactoryImpl @Inject constructor(val provider: Provider<ChaptersFragment>) : ViewFactory {
    override fun createChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView {
        return delegate.contentView.getView("orange_chapters_button")
    }

    override fun getChaptersFragment(): ChaptersFragment {
        return provider.get()
    }
}
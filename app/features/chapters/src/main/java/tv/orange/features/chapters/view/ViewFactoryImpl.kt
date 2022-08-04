package tv.orange.features.chapters.view

import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import tv.orange.features.chapters.data.view.ChaptersFragment
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import javax.inject.Inject
import javax.inject.Provider

class ViewFactoryImpl @Inject constructor(val provider: Provider<ChaptersFragment>) : ViewFactory {
    override fun createChaptersButton(delegate: PlayerOverlayViewDelegate): ImageView? {
        val id = delegate.context.resources.getIdentifier(
            "orange_chapters_button",
            "id",
            delegate.context.packageName
        )
        if (id == 0) {
            return null
        }

        return delegate.contentView.findViewById(id)
    }

    override fun getChaptersFragment(): ChaptersFragment {
        return provider.get()
    }
}
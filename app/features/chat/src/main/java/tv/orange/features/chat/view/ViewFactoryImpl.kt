package tv.orange.features.chat.view

import android.widget.ImageView
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.features.chat.bridge.ScrollableOrangeSection
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createOrangeEmotesButton(delegate: EmotePickerViewDelegate): ImageView? {
        val id = delegate.context.resources.getIdentifier(
            "orange_emotes_button",
            "id",
            delegate.context.packageName
        )
        if (id == 0) {
            return null
        }

        return delegate.contentView.findViewById<ImageView?>(id).apply {
            setOnClickListener {
                delegate.cast<ScrollableOrangeSection>().scrollToOrangeSection()
            }
        }
    }
}
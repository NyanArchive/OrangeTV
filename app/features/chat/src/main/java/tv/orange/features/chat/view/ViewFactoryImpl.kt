package tv.orange.features.chat.view

import android.widget.ImageView
import tv.orange.core.compat.ClassCompat
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.chat.bridge.ScrollableOrangeSection
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createOrangeEmotesButton(delegate: EmotePickerViewDelegate): ImageView {
        return delegate.contentView.getView<ImageView>(resName = "orange_emotes_button").apply {
            setOnClickListener {
                ClassCompat.invokeIf<ScrollableOrangeSection>(delegate) {
                    it.scrollToOrangeSection()
                }
            }
        }
    }
}
package tv.orange.features.chat.view

import android.widget.ImageView
import tv.twitch.android.shared.emotes.emotepicker.EmotePickerViewDelegate

interface ViewFactory {
    fun createOrangeEmotesButton(delegate: EmotePickerViewDelegate): ImageView
}
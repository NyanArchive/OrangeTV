package tv.orange.features.pronouns

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.widget.TextView
import tv.orange.features.pronouns.bridge.RoundedBackgroundSpan
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem
import java.lang.ref.WeakReference

class PronounSetter(view: MessageRecyclerItem.ChatMessageViewHolder) {
    private val ref = WeakReference(view)

    fun setText(pronounText: String) {
        val text = ref.get()?.messageTextView?.text ?: return
        val spanned = text as Spanned

        val ssb = SpannableStringBuilder(pronounText).apply {
            setSpan(
                RoundedBackgroundSpan(Color.RED, Color.WHITE, 16, 0F),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        ref.get()?.messageTextView?.setText(
            SpannedString(ssb.append(" ").append(spanned)),
            TextView.BufferType.SPANNABLE
        )
    }

    fun destroy() {
        ref.clear()
    }
}
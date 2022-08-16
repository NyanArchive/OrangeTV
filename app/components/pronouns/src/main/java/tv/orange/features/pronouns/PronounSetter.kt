package tv.orange.features.pronouns

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.SpannedString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem
import java.lang.ref.WeakReference

class PronounSetter(view: MessageRecyclerItem.ChatMessageViewHolder) {
    private val ref = WeakReference(view)

    fun setPronoun(data: String) {
        val text = ref.get()?.messageTextView?.text ?: return
        val spanned = text as Spanned

        val pronounText = SpannableStringBuilder(data)
        pronounText.setSpan(
            BackgroundColorSpan(Color.RED),
            0,
            pronounText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        pronounText.setSpan(
            ForegroundColorSpan(Color.WHITE),
            0,
            pronounText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val res = SpannedString(pronounText.append(" ").append(spanned))
        ref.get()?.messageTextView?.setText(res, TextView.BufferType.SPANNABLE)
    }

    fun destroy() {
        ref.clear()
    }

    fun getSpanned(): Spanned? {
        return ref.get()?.messageTextView?.text as Spanned?
    }
}
package tv.orange.features.chat.util

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.LruCache
import android.util.TypedValue
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.shared.chat.pub.ChatMessageInterface
import tv.twitch.android.shared.chat.util.ClickableUsernameSpan
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan
import tv.twitch.android.shared.ui.elements.span.UrlDrawable
import java.text.SimpleDateFormat
import java.util.*

object ChatUtil {
    private const val TIMESTAMP_DATE_FORMAT = "HH:mm"

    private const val MIN_DARK_THEME_NICKNAME_HSV_VALUE = .3f
    private const val FIXED_DARK_THEME_NICKNAME_HSV_VALUE = .4f
    private const val MIN_DEFAULT_THEME_NICKNAME_HSV_VALUE = .9f
    private const val FIXED_DEFAULT_THEME_NICKNAME_HSV_VALUE = .8f

    private const val CACHE_SIZE = 500

    private val darkThemeCache: LruCache<Int, Int> = object : LruCache<Int, Int>(CACHE_SIZE) {
        override fun create(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            if (hsv[2] >= MIN_DARK_THEME_NICKNAME_HSV_VALUE) {
                return color
            }
            hsv[2] = FIXED_DARK_THEME_NICKNAME_HSV_VALUE
            return Color.HSVToColor(hsv)
        }
    }

    private val defaultThemeCache: LruCache<Int, Int> = object : LruCache<Int, Int>(CACHE_SIZE) {
        override fun create(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            if (hsv[2] <= MIN_DEFAULT_THEME_NICKNAME_HSV_VALUE) {
                return color
            }
            hsv[2] = FIXED_DEFAULT_THEME_NICKNAME_HSV_VALUE
            return Color.HSVToColor(hsv)
        }
    }

    fun fixUsernameColor(color: Int, isDarkThemeEnabled: Boolean): Int {
        return if (isDarkThemeEnabled) {
            darkThemeCache[color]
        } else {
            defaultThemeCache[color]
        }
    }

    fun createDeletedGrey(message: Spanned?): Spanned? {
        if (message.isNullOrBlank()) {
            return message
        }

        message.getSpans(0, message.length, ForegroundColorSpan::class.java)?.forEach { colorSpan ->
            if (colorSpan.foregroundColor == Color.GRAY) {
                return message
            }
        }

        val message = SpannableStringBuilder(message)

        message.getSpans(0, message.length, ForegroundColorSpan::class.java)?.forEach {
            message.removeSpan(it)
        }
        message.getSpans(0, message.length, CenteredImageSpan::class.java)?.forEach {
            val drawable = it.imageDrawable
            if (drawable is UrlDrawable) {
                drawable.setGrey(true)
            }
        }
        message.setSpan(
            ForegroundColorSpan(Color.GRAY),
            0,
            message.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return SpannedString.valueOf(message)
    }

    fun createDeletedStrikethrough(message: Spanned?): Spanned? {
        if (message.isNullOrBlank()) {
            return message
        }

        message.getSpans(0, message.length, StrikethroughSpan::class.java)?.let { spans ->
            if (spans.isNotEmpty()) {
                return message
            }
        }

        return SpannedString.valueOf(SpannableStringBuilder(message).apply {
            setSpan(
                StrikethroughSpan(),
                getMessageStartPos(message = message),
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        })
    }

    fun getMessageStartPos(message: Spanned): Int {
        var usernameEndPos = 0
        val spans = message.getSpans(0, message.length, ClickableUsernameSpan::class.java)
        if (spans.isEmpty()) {
            return usernameEndPos
        }
        usernameEndPos = message.getSpanEnd(spans[0])
        val pos = usernameEndPos + 2
        if (pos < message.length) {
            if (TextUtils.equals(message.subSequence(usernameEndPos, pos), ": ")) {
                usernameEndPos = pos
            }
        }
        return usernameEndPos
    }

    fun formatTimestamp(message: Spanned, timestamp: CharSequence): Spanned {
        return SpannableString.valueOf(SpannableStringBuilder(timestamp).append(" ").append(message))
    }

    fun createTimestampSpanFromChatMessageSpan(message: Spanned, date: Date): Spanned {
        return formatTimestamp(
            message = message,
            timestamp = SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.ENGLISH).format(date)
        )
    }

    fun isUserMentioned(
        cmi: ChatMessageInterface,
        username: String
    ): Boolean {
        if (username.isBlank()) {
            return false
        }

        return cmi.tokens.any { token ->
            if (token is MessageToken.MentionToken) {
                token.userName?.let { username.equals(it, ignoreCase = true) } ?: false
            } else {
                false
            }
        }
    }

    fun spToPx(context: Context, sp: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp.toFloat(),
            context.resources.displayMetrics
        )
    }
}
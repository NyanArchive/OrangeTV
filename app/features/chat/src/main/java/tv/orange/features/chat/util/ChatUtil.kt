package tv.orange.features.chat.util

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.LruCache
import android.util.TypedValue
import okhttp3.internal.toHexString
import tv.orange.core.Logger
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface
import tv.twitch.android.shared.chat.util.ClickableUsernameSpan
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan
import tv.twitch.android.shared.ui.elements.span.UrlDrawable
import java.text.SimpleDateFormat
import java.util.*

object ChatUtil {
    private const val TIMESTAMP_DATE_FORMAT = "HH:mm"

    private const val MIN_DARK_THEME_NICKNAME_HSV_VALUE = .3f
    private const val FIXED_DARK_THEME_NICKNAME_HSV_VALUE = .4f
    private const val MIN_WHITE_THEME_NICKNAME_HSV_VALUE = .9f
    private const val FIXED_WHITE_THEME_NICKNAME_HSV_VALUE = .8f

    private const val CACHE_SIZE = 500

    private val darkThemeCache: LruCache<Int, Int> = object : LruCache<Int, Int>(CACHE_SIZE) {
        override fun create(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            if (hsv[2] >= MIN_DARK_THEME_NICKNAME_HSV_VALUE) {
                return color
            }
            hsv[2] = FIXED_DARK_THEME_NICKNAME_HSV_VALUE
            val res = Color.HSVToColor(hsv)
            Logger.debug("org: ${color.toHexString()}, fixed: ${res.toHexString()}")
            return res
        }
    }

    private val lightThemeCache: LruCache<Int, Int> = object : LruCache<Int, Int>(CACHE_SIZE) {
        override fun create(color: Int): Int {
            val hsv = FloatArray(3)
            Color.colorToHSV(color, hsv)
            if (hsv[2] <= MIN_WHITE_THEME_NICKNAME_HSV_VALUE) {
                return color
            }
            hsv[2] = FIXED_WHITE_THEME_NICKNAME_HSV_VALUE
            val res = Color.HSVToColor(hsv)
            Logger.debug("org: ${color.toHexString()}, fixed: ${res.toHexString()}")
            return res
        }
    }

    fun fixUsernameColor(color: Int, darkTheme: Boolean): Int {
        return if (darkTheme) {
            darkThemeCache[color]
        } else {
            lightThemeCache[color]
        }
    }

    fun createDeletedGrey(msg: Spanned?): Spanned? {
        if (msg.isNullOrBlank()) {
            return msg
        }

        msg.getSpans(0, msg.length, ForegroundColorSpan::class.java)?.forEach {
            if (it.foregroundColor == Color.GRAY) {
                return msg
            }
        }

        val message = SpannableStringBuilder(msg)
        message.getSpans(0, msg.length, ForegroundColorSpan::class.java)?.forEach {
            message.removeSpan(it)
        }

        message.getSpans(0, msg.length, CenteredImageSpan::class.java)?.forEach {
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

    fun createDeletedStrikethrough(msg: Spanned?): Spanned? {
        if (msg.isNullOrBlank()) {
            return msg
        }

        msg.getSpans(0, msg.length, StrikethroughSpan::class.java)?.let { spans ->
            if (spans.isNotEmpty()) {
                return msg
            }
        }

        val message = SpannableStringBuilder(msg).apply {
            setSpan(
                StrikethroughSpan(),
                getMessageStartPos(msg),
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return SpannedString.valueOf(message)
    }

    fun getMessageStartPos(msg: Spanned): Int {
        var usernameEndPos = 0
        val spans = msg.getSpans(0, msg.length, ClickableUsernameSpan::class.java)
        if (spans.isEmpty()) {
            return usernameEndPos
        }
        usernameEndPos = msg.getSpanEnd(spans[0])
        val pos = usernameEndPos + 2
        if (pos < msg.length) {
            if (TextUtils.equals(msg.subSequence(usernameEndPos, pos), ": ")) {
                usernameEndPos = pos
            }
        }
        return usernameEndPos
    }

    fun formatTimestamp(msg: Spanned, timestamp: CharSequence): Spanned =
        SpannableString.valueOf(SpannableStringBuilder(timestamp).append(" ").append(msg))

    fun createTimestampSpanFromChatMessageSpan(msg: Spanned, date: Date): Spanned =
        formatTimestamp(
            msg = msg,
            timestamp = SimpleDateFormat(TIMESTAMP_DATE_FORMAT, Locale.ENGLISH).format(date)
        )

    fun isUserMentioned(
        chatMessageInterface: ChatMessageInterface,
        username: String
    ): Boolean {
        if (username.isBlank()) {
            return false
        }

        return chatMessageInterface.tokens.any { token ->
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
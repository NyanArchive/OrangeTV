package tv.orange.features.pronouns.bridge

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.LineHeightSpan
import android.text.style.ReplacementSpan

/* ...
    Source: https://gist.github.com/anacicconi/39469b975db3edb81adce3a7d199133b
 */
class RoundedBackgroundSpan(
    private val backgroundColor: Int,
    private val textColor: Int,
    private val padding: Int = 16,
    private val radius: Float? = null
) : ReplacementSpan(),

    LineHeightSpan {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return (padding + paint.measureText(text, start, end) + padding).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.color = backgroundColor

        val width = paint.measureText(text, start, end)
        val height = paint.fontMetrics.descent - paint.fontMetrics.ascent + 2 * padding

        if (radius != null) {
            val rect = RectF(x, top.toFloat(), x + width + 2 * padding, bottom.toFloat())
            canvas.drawRoundRect(rect, radius, radius, paint)
        } else {
            canvas.drawCircle(
                x + ((width + 2 * padding) / 2),
                ((bottom - top) / 2).toFloat(),
                height / 2,
                paint
            )
        }

        paint.color = textColor
        canvas.drawText(text, start, end, x + padding, y.toFloat(), paint)
    }

    override fun chooseHeight(
        text: CharSequence,
        start: Int,
        end: Int,
        spanstartv: Int,
        lineHeight: Int,
        fm: Paint.FontMetricsInt?
    ) {
    }
}
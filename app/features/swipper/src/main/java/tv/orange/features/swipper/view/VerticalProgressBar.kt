package tv.orange.features.swipper.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.widget.ProgressBar

class VerticalProgressBar(context: Context) : ProgressBar(
    context,
    null,
    android.R.attr.progressBarStyleHorizontal
) {
    companion object {
        private const val BACKGROUND_COLOR = Color.WHITE
        private const val BORDER_WIDTH = 1

        private val PROGRESS_COLOR = Color.parseColor("#6441A5")

        private fun generateProgressDrawable(): Drawable {
            val backgroundShape = GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                intArrayOf(BACKGROUND_COLOR, BACKGROUND_COLOR, BACKGROUND_COLOR)
            )
            val progressShape = GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                intArrayOf(PROGRESS_COLOR, PROGRESS_COLOR, PROGRESS_COLOR)
            )
            backgroundShape.setStroke(BORDER_WIDTH, Color.BLACK)
            val progressClip = ClipDrawable(progressShape, Gravity.BOTTOM, ClipDrawable.VERTICAL)
            val layers = arrayOf(backgroundShape, progressClip)
            return LayerDrawable(layers)
        }
    }

    init {
        progressDrawable = generateProgressDrawable()
        id = generateViewId()
        setBackgroundColor(BACKGROUND_COLOR)
    }
}
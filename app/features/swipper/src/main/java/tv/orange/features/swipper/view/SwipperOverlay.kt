package tv.orange.features.swipper.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import tv.orange.core.util.ViewUtil.dipToPix

class SwipperOverlay(context: Context) : RelativeLayout(context) {
    private val volumeProgressBar = VerticalProgressBar(context)
    private val brightnessProgressBar = VerticalProgressBar(context)
    private val progress = TextView(context)

    fun prepare() {
        id = generateViewId()
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val volumeBarParams = LayoutParams(dipToPix(14), dipToPix(110)).apply {
            addRule(CENTER_VERTICAL)
            setMargins(dipToPix(12), 0, dipToPix(10), 0)
        }

        this.addView(volumeProgressBar.apply {
            layoutParams = volumeBarParams
            visibility = INVISIBLE
        }, volumeBarParams)

        val brightnessBarParams = LayoutParams(dipToPix(14), dipToPix(110)).apply {
            addRule(CENTER_VERTICAL)
            addRule(ALIGN_PARENT_RIGHT)
            setMargins(dipToPix(12), 0, dipToPix(10), 0)
        }

        this.addView(brightnessProgressBar.apply {
            layoutParams = brightnessBarParams
            max = MAX_BRIGHTNESS
            visibility = INVISIBLE
        }, brightnessBarParams)

        val textParam = LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(CENTER_VERTICAL)
            addRule(CENTER_HORIZONTAL)
        }
        this.addView(progress.apply {
            id = generateViewId()
            textSize = PROGRESS_TEXT_SIZE.toFloat()
            setTextColor(Color.WHITE)
            setPadding(5, 5, 5, 5)
            setShadowLayer(SHADOW_SIZE, 0.0f, 0.0f, Color.BLACK)

            layoutParams = textParam
            visibility = INVISIBLE
        }, textParam)
    }

    fun showVolume() {
        volumeProgressBar.animate().cancel()
        if (volumeProgressBar.visibility != VISIBLE) {
            volumeProgressBar.alpha = 1f
            volumeProgressBar.visibility = VISIBLE
        }
        showProgressText()
    }

    private fun showProgressText() {
        progress.animate().cancel()
        if (progress.visibility != VISIBLE) {
            progress.alpha = 1f
            progress.visibility = VISIBLE
        }
    }

    fun showBrightness() {
        brightnessProgressBar.animate().cancel()
        if (brightnessProgressBar.visibility != VISIBLE) {
            brightnessProgressBar.alpha = 1f
            brightnessProgressBar.visibility = VISIBLE
        }
        showProgressText()
    }

    private fun hideProgressText() {
        progress.animate().cancel()
        progress.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progress.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                    progress.visibility = GONE
                }
            })
    }

    fun hideVolume() {
        volumeProgressBar.animate().cancel()
        volumeProgressBar.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    volumeProgressBar.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                    volumeProgressBar.visibility = GONE
                }
            })
        hideProgressText()
    }

    fun setVolume(value: Int) {
        volumeProgressBar.progress = value
        progress.text = value.toString()
    }

    var maxVolume: Int
        get() = volumeProgressBar.max
        set(max) {
            volumeProgressBar.max = max
        }

    fun hideBrightness() {
        brightnessProgressBar.animate().cancel()
        brightnessProgressBar.animate()
            .alpha(0f)
            .setDuration(ANIMATION_DURATION.toLong())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    brightnessProgressBar.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator) {
                    brightnessProgressBar.visibility = GONE
                }
            })
        hideProgressText()
    }

    fun setBrightness(value: Int) {
        brightnessProgressBar.progress = value
        progress.text = value.toString()
    }

    val maxBrightness: Int
        get() = brightnessProgressBar.max

    companion object {
        private const val ANIMATION_DURATION = 500
        private const val MAX_BRIGHTNESS = 100
        private const val PROGRESS_TEXT_SIZE = 45
        private const val SHADOW_SIZE = 2.0f
    }
}
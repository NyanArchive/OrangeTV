package tv.orange.features.streamuptime.bridge

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("AppCompatCustomView")
class StreamUptimeView constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs, android.R.attr.textViewStyle) {
    private var tick = -1
    private var task: Runnable? = null

    private var defaultViewTextResId = -1

    private var canRestoreState = false

    private fun drawCurrentTime() {
        text = DateUtils.formatElapsedTime(tick.toLong())
    }

    fun showUptime(seconds: Int) {
        setupTimer(seconds)
    }

    fun showIndicator() {
        destroyTimer()
        if (defaultViewTextResId != -1) {
            setText(defaultViewTextResId)
        }
    }

    fun setTextFromResources(resId: Int) {
        defaultViewTextResId = resId
        if (tick == -1) {
            setText(defaultViewTextResId)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!canRestoreState) {
            return
        }
        if (tick >= 0) {
            setupTimer(tick)
        } else {
            if (defaultViewTextResId != -1) {
                setTextFromResources(defaultViewTextResId)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        destroyTimer()
        canRestoreState = true
    }

    private fun setupTimer(seconds: Int) {
        destroyTimer()
        tick = seconds
        task = object : Runnable {
            override fun run() {
                drawCurrentTime()
                tick++
                handler.postDelayed(this, 1000)
            }
        }
        task?.run()
    }

    private fun destroyTimer() {
        task?.let {
            handler.removeCallbacks(it)
        }
        task = null
    }
}
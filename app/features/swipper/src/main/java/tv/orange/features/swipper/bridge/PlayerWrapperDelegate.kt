package tv.orange.features.swipper.bridge

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.media.AudioManager
import android.os.Handler
import android.view.*
import android.widget.RelativeLayout
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.isHit
import tv.orange.core.util.ViewUtil.isVisible
import tv.orange.features.swipper.util.BrightnessHelper.getWindowBrightness
import tv.orange.features.swipper.util.BrightnessHelper.setWindowBrightness
import tv.orange.features.swipper.view.SwipperOverlay
import kotlin.math.*

class PlayerWrapperDelegate(private val wrapper: PlayerWrapper, private val context: Activity) {
    private val overlay = SwipperOverlay(context = context)
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val handler = Handler(context.mainLooper)
    private val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop * 2
    private val mPaddingDeviceIgnore: Int =
        (PADDING_DEFAULT_IGNORE * context.resources.displayMetrics.density).roundToInt()

    private var progressHideTask: Runnable? = null

    private var inBrightnessArea = false
    private var oldVolume = 0
    private var oldBrightness = 0
    private var isVolumeSwipeEnabled = false
    private var isBrightnessSwipeEnabled = false
    private var initTouchPosX = -1f
    private var initTouchPosY = -1f
    private var inScrollArea = false
    private var inScrollMode = false

    private val hitRect = Rect()

    fun setOverlay(viewGroup: ViewGroup) {
        val relativeLayout = RelativeLayout(context).apply {
            gravity = Gravity.CENTER
            addView(this@PlayerWrapperDelegate.overlay)
        }
        val overlayParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        viewGroup.addView(relativeLayout, overlayParams)
        overlay.maxVolume = systemMaxVolume
        overlay.setVolume(systemVolume)
        overlay.setBrightness(getWindowBrightness(context))
        overlay.visibility = View.VISIBLE
        overlay.invalidate()
        overlay.requestLayout()
    }

    fun setVolumeEnabled(state: Boolean) {
        isVolumeSwipeEnabled = state
    }

    fun setBrightnessEnabled(state: Boolean) {
        isBrightnessSwipeEnabled = state
    }

    val systemMaxVolume: Int
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    var systemVolume: Int
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        set(index) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0)
        }
    private val overlayHeight: Int
        get() = overlay.height

    private val maxVolume: Int
        get() = overlay.maxVolume

    private val maxBrightness: Int
        get() = overlay.maxBrightness

    private fun calculate(delta: Float, oldStep: Int, max: Int): Int {
        val height = overlayHeight * GESTURE_SCALE_FACTORY
        val step = height / max
        val diff = (delta / step).toInt()
        return max(0, min(max, oldStep + diff))
    }

    private fun updateVolumeProgress(delta: Float) {
        val value = calculate(delta = delta, oldStep = oldVolume, max = maxVolume)
        systemVolume = value
        overlay.setVolume(value = value)
        overlay.showVolume()
    }

    private fun updateBrightnessProgress(delta: Float) {
        val value = calculate(delta = delta, oldStep = oldBrightness, max = maxBrightness)
        setWindowBrightness(context = context, value = value)
        overlay.setBrightness(value = value)
        overlay.showBrightness()
    }

    private fun delayHideProgress() {
        progressHideTask?.let {
            synchronized(handler) {
                progressHideTask?.let {
                    handler.removeCallbacks(it)
                    progressHideTask = null
                }
            }
        }

        progressHideTask = Runnable { hideProgress() }
        progressHideTask?.let {
            handler.postDelayed(it, DELAY_TIMEOUT_MILLISECONDS.toLong())
        }
    }

    private fun hideProgress() {
        progressHideTask?.let {
            synchronized(handler) {
                progressHideTask?.let {
                    handler.removeCallbacks(it)
                    progressHideTask = null
                }
            }
        }
        overlay.hideVolume()
        overlay.hideBrightness()
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (inScrollMode) {
                    delayHideProgress()
                }
                inScrollMode = false
                return false
            }
            MotionEvent.ACTION_DOWN -> {
                oldBrightness = getWindowBrightness(context = context)
                oldVolume = systemVolume
                inBrightnessArea = event.x < overlay.width / 2.0f
                initTouchPosX = event.x
                initTouchPosY = event.y
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val delta = initTouchPosY - event.y
                if (!inScrollMode) {
                    hideProgress()
                    inScrollMode = true
                }
                if (inBrightnessArea) {
                    updateBrightnessProgress(delta = delta)
                } else {
                    updateVolumeProgress(delta = delta)
                }
                return true
            }
        }
        return false
    }

    fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                initTouchPosX = -1f
                initTouchPosY = -1f
                onTouchEvent(event = motionEvent)
                return false
            }
            MotionEvent.ACTION_DOWN -> {
                initTouchPosY = motionEvent.y.roundToLong().toFloat()
                initTouchPosX = motionEvent.x.roundToLong().toFloat()
                inScrollArea = checkArea(event = motionEvent)
                if (inScrollArea) {
                    onTouchEvent(event = motionEvent)
                }
                return false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!inScrollArea) {
                    return false
                }
                if (motionEvent.pointerCount > 1) {
                    inScrollArea = false
                    return false
                }
                if (abs(initTouchPosY - motionEvent.y) > mTouchSlop) {
                    if (inBrightnessArea) {
                        if (!isBrightnessSwipeEnabled) return false
                    } else {
                        if (!isVolumeSwipeEnabled) return false
                    }
                    return onTouchEvent(event = motionEvent)
                }
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                inScrollArea = false
                return false
            }
        }
        return false
    }

    private fun checkCollisions(): Boolean {
        if (!wrapper.playerOverlayContainer.isHit(
                rect = hitRect,
                x = initTouchPosX.toInt(),
                y = initTouchPosY.toInt()
            )
        ) {
            return false
        }

        val debugPanel = wrapper.debugPanelContainer
        if (debugPanel.childCount > 0) {
            val list = debugPanel.getView<ViewGroup>("video_debug_list")
            if (list.isVisible() &&
                list.isHit(rect = hitRect, x = initTouchPosX.toInt(), y = initTouchPosY.toInt())
            ) return false
        }
        return true
    }

    private fun checkArea(event: MotionEvent): Boolean {
        if (initTouchPosY <= mPaddingDeviceIgnore) {
            return false
        }
        val overlayBottomY = wrapper.playerOverlayContainer.let {
            it.y + it.height
        }
        if (initTouchPosY >= overlayBottomY - mPaddingDeviceIgnore) {
            return false
        }
        if (event.pointerCount > 1) {
            return false
        }
        if (context.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        return checkCollisions()
    }

    companion object {
        private const val PADDING_DEFAULT_IGNORE = 30
        private const val DELAY_TIMEOUT_MILLISECONDS = 500
        private const val GESTURE_SCALE_FACTORY = 0.7f
    }
}
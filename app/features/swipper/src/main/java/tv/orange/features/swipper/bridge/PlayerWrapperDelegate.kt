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

class PlayerWrapperDelegate(
    private val wrapper: PlayerWrapper
) {
    private val context = wrapper.context
    private val handler = Handler(context.mainLooper)

    private val overlay = SwipperOverlay(context = context)

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop * 2
    private val paddingDeviceIgnore: Int = calcPaddingDeviceIgnore(context = context)

    private var progressHideTask: Runnable? = null

    private var inBrightnessArea = false
    private var currentVolume = 0
    private var currentBrightness = 0
    private var initTouchPosX = -1f
    private var initTouchPosY = -1f
    private var inScrollArea = false
    private var inScrollMode = false

    private val hitRect = Rect()

    var isVolumeSwipeEnabled = false
    var isBrightnessSwipeEnabled = false

    fun setOverlayToPlayerContainer(viewGroup: ViewGroup) {
        overlay.prepare()
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
        overlay.setBrightness(getProcFromBrightness(brightness = getWindowBrightness(context as Activity)))
        overlay.visibility = View.VISIBLE
        overlay.invalidate()
        overlay.requestLayout()
    }

    private val systemMaxVolume: Int
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)

    private var systemVolume: Int
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        set(index) = audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0)

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
        val value = calculate(delta = delta, oldStep = currentVolume, max = maxVolume)
        systemVolume = value
        overlay.setVolume(value = value)
        overlay.showVolume()
    }

    private fun updateBrightnessProgress(delta: Float) {
        val value = calculate(delta = delta, oldStep = currentBrightness, max = maxBrightness)
        setWindowBrightness(activity = context as Activity, value = getBrightnessFromProc(value))
        overlay.setBrightness(value = value)
        overlay.showBrightness()
    }

    private fun clearProgressHideTask() {
        progressHideTask?.let {
            synchronized(handler) {
                progressHideTask?.let {
                    handler.removeCallbacks(it)
                    progressHideTask = null
                }
            }
        }
    }

    private fun delayHideProgress() {
        clearProgressHideTask()

        progressHideTask = Runnable { hideProgress() }
        progressHideTask?.let {
            handler.postDelayed(it, DELAY_TIMEOUT_MILLISECONDS.toLong())
        }
    }

    private fun hideProgress() {
        clearProgressHideTask()

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
                currentBrightness = getProcFromBrightness(
                    brightness = getWindowBrightness(activity = context as Activity)
                )
                currentVolume = systemVolume
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
                if (abs(initTouchPosY - motionEvent.y) > touchSlop) {
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
            val list = debugPanel.getView<ViewGroup>(VIDEO_DEBUG_LIST_ID)
            if (list.isVisible() && list.isHit(
                    rect = hitRect,
                    x = initTouchPosX.toInt(),
                    y = initTouchPosY.toInt()
                )
            ) return false
        }
        return true
    }

    private fun checkArea(event: MotionEvent): Boolean {
        if (initTouchPosY <= paddingDeviceIgnore) {
            return false
        }
        if (event.pointerCount > 1) {
            return false
        }
        if (context.resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        val overlayBottomY = wrapper.playerOverlayContainer.let {
            it.y + it.height
        }
        if (initTouchPosY >= overlayBottomY - paddingDeviceIgnore) {
            return false
        }
        return checkCollisions()
    }

    companion object {
        private const val PADDING_DEFAULT_IGNORE = 30
        private const val DELAY_TIMEOUT_MILLISECONDS = 500
        private const val GESTURE_SCALE_FACTORY = 0.7f
        private const val VIDEO_DEBUG_LIST_ID = "video_debug_list"

        private fun calcPaddingDeviceIgnore(context: Context): Int {
            return (PADDING_DEFAULT_IGNORE * context.resources.displayMetrics.density).roundToInt()
        }

        private fun getProcFromBrightness(brightness: Float): Int {
            return max(0, min(100, (brightness * 100).toInt()))
        }

        private fun getBrightnessFromProc(proc: Int): Float {
             return min(1.0f, max(0.01f, proc.toFloat().div(100)))
        }
    }
}
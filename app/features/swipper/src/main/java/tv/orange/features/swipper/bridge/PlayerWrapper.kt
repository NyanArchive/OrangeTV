package tv.orange.features.swipper.bridge

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.util.ViewUtil.getView

class PlayerWrapper @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var playerOverlayContainer: ViewGroup
    lateinit var debugPanelContainer: ViewGroup

    private val needInterceptTouchEvents = let {
        Flag.VOLUME_GESTURE.asBoolean() || Flag.BRIGHTNESS_GESTURE.asBoolean()
    }

    private val playerWrapperDelegate = PlayerWrapperDelegate(
        wrapper = this
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        playerOverlayContainer = getView("player_overlay_container")
        debugPanelContainer = getView("debug_panel_container")
        playerWrapperDelegate.setOverlayToPlayerContainer(viewGroup = playerOverlayContainer)
        playerWrapperDelegate.isVolumeSwipeEnabled = Flag.VOLUME_GESTURE.asBoolean()
        playerWrapperDelegate.isBrightnessSwipeEnabled = Flag.BRIGHTNESS_GESTURE.asBoolean()
    }

    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        return if (needInterceptTouchEvents) {
            playerWrapperDelegate.onInterceptTouchEvent(motionEvent = motionEvent)
        } else {
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        return playerWrapperDelegate.onTouchEvent(event = motionEvent)
    }
}
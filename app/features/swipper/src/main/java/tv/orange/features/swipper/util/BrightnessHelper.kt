package tv.orange.features.swipper.util

import android.app.Activity
import kotlin.math.max

object BrightnessHelper {
    fun getWindowBrightness(activity: Activity): Float {
        return max(activity.window.attributes.screenBrightness, 0f)
    }

    fun setWindowBrightness(activity: Activity, value: Float) {
        activity.window.apply {
            attributes = attributes.apply {
                screenBrightness = max(0.01f, value)
            }
        }
    }
}
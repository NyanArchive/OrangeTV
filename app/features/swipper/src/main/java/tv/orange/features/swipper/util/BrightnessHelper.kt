package tv.orange.features.swipper.util

import android.app.Activity
import kotlin.math.max

object BrightnessHelper {
    fun getWindowBrightness(context: Activity): Int {
        return max((context.window.attributes.screenBrightness * 100).toInt(), 0)
    }

    fun setWindowBrightness(context: Activity, value: Int) {
        context.window.attributes = context.window.attributes.apply {
            screenBrightness = max(0.01f, value.toFloat().div(100))
        }
    }
}
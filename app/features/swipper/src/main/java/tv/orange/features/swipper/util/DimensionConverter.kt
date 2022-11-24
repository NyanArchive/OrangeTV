package tv.orange.features.swipper.util

import android.content.Context
import android.util.TypedValue

object DimensionConverter {
    fun dipToPix(context: Context, dip: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
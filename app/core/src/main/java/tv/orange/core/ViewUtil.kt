package tv.orange.core

import android.view.View

object ViewUtil {
    fun <T : View> View.getView(resName: String): T {
        return this.findViewById(ResourceManager.getResId(resName))
    }
}
package tv.orange.core.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tv.orange.core.ResourceManager

@Suppress("UNCHECKED_CAST")
object ViewUtil {
    fun <T : View> View.getView(resName: String): T {
        return this.findViewById(ResourceManager.get().getId(resName = resName))
    }

    fun <T : View> Activity.getView(resName: String): T {
        return this.findViewById(ResourceManager.get().getId(resName = resName))
    }

    fun Activity.setContentView(resName: String) {
        this.setContentView(ResourceManager.get().getLayoutId(resName))
    }

    fun LayoutInflater.inflate(container: ViewGroup?, resName: String): View {
        val id = ResourceManager.get().getLayoutId(resName = resName)
        if (id == 0 || id == -1) {
            throw Resources.NotFoundException(resName)
        }

        return this.inflate(
            id,
            container,
            false
        )
    }

    fun ViewGroup.inflate(inflater: LayoutInflater, resName: String): View {
        val id = ResourceManager.get().getLayoutId(resName = resName)
        if (id == 0 || id == -1) {
            throw Resources.NotFoundException(resName)
        }

        return inflater.inflate(
            id,
            this,
            false
        )
    }

    fun ViewGroup.inflate(resName: String): View {
        return this.inflate(LayoutInflater.from(context), resName)
    }

    fun Context.inflate(resName: String): View {
        val id = ResourceManager.get().getLayoutId(resName = resName)
        if (id == 0 || id == -1) {
            throw Resources.NotFoundException(resName)
        }

        return View.inflate(this, id, null)
    }

    fun View.changeVisibility(isVisible: Boolean) {
        this.visibility = if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun show(vararg views: View) {
        views.forEach { it.changeVisibility(true) }
    }

    fun hide(vararg views: View) {
        views.forEach { it.changeVisibility(false) }
    }

    fun ViewGroup.isHit(rect: Rect, x: Int, y: Int): Boolean {
        this.getHitRect(rect)
        return rect.contains(x, y)
    }

    fun View.isVisible(): Boolean {
        return this.visibility == View.VISIBLE
    }

    fun ViewGroup.getFirstChild(): View? {
        return this.getChildAt(0)
    }
}
package tv.orange.core

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

@Suppress("UNCHECKED_CAST")
object ViewUtil {
    fun <T : View> View.getView(resName: String): T {
        return this.findViewById(ResourceManager.get().getId(resName))
    }

    fun LayoutInflater.inflate(container: ViewGroup?, resName: String): View {
        val id = ResourceManager.get().getLayoutId(resName)
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
        val id = ResourceManager.get().getLayoutId(resName)
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
}
package tv.orange.core

import android.content.Context
import android.util.LruCache
import tv.orange.models.abc.Feature
import tv.twitch.android.app.core.ApplicationContext

class ResourceManager(private val context: Context) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ResourceManager::class.java)
    }

    private enum class Id(private val cache: Cache) {
        ID(Cache("id")),
        STRING(Cache("string")),
        DRAWABLE(Cache("drawable")),
        COLOR(Cache("color")),
        LAYOUT(Cache("layout"));

        fun get(resName: String): Int {
            return cache.get(resName)
        }
    }

    private class Cache(
        private val type: String
    ) : LruCache<String, Int>(100) {
        private val context by lazy {
            ApplicationContext.getInstance().getContext()
        }

        override fun create(key: String): Int {
            return context.resources.getIdentifier(key, type, context.packageName)
        }
    }

    fun getId(resName: String): Int {
        return Id.ID.get(resName)
    }

    fun getLayoutId(resName: String): Int {
        return Id.LAYOUT.get(resName)
    }

    fun getStringId(resName: String): Int {
        return Id.STRING.get(resName)
    }

    fun getDrawableId(resName: String): Int {
        return Id.DRAWABLE.get(resName)
    }

    fun getColorId(resName: String): Int {
        return Id.COLOR.get(resName)
    }

    fun getString(resName: String): String {
        val id = Id.STRING.get(resName)

        return if (id == 0 || id == -1) {
            "$resName: ID NOT FOUND"
        } else {
            context.getString(id)
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
package tv.orange.core.models

import android.util.LruCache
import tv.twitch.android.app.core.ApplicationContext

class ResIdCache(
    private val resType: String,
    cacheSize: Int
) : LruCache<String, Int>(cacheSize) {
    private val context by lazy {
        ApplicationContext.getInstance().getContext()
    }

    override fun create(resName: String): Int {
        return context.resources.getIdentifier(resName, resType, context.packageName)
    }
}
package tv.orange.core.models

import android.util.LruCache
import tv.twitch.android.app.core.ApplicationContext

class ResIdCache(
    private val type: String,
    size: Int
) : LruCache<String, Int>(size) {
    private val context by lazy {
        ApplicationContext.getInstance().getContext()
    }

    override fun create(key: String): Int {
        return context.resources.getIdentifier(key, type, context.packageName)
    }
}
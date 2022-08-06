package tv.orange.core.compat

import javax.inject.Provider

object ClassCompat {
    inline fun <reified T> Any.cast(): T {
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getProvider(component: Any, fieldName: String): Provider<T> {
        return component::class.java.getDeclaredField(fieldName).apply {
            isAccessible = true
        }.get(component) as Provider<T>
    }
}
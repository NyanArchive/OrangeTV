package tv.orange.core.compat

import tv.orange.core.LoggerImpl

object ClassCompat {
    inline fun <reified T> Any.cast(): T {
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> Any.getPrivateField(fieldName: String): T {
        return this::class.java.getDeclaredField(fieldName).apply {
            isAccessible = true
        }.get(this) as T
    }

    inline fun <reified T> invokeIf(obj: Any, function: (obj: T) -> Unit) {
        if (obj is T) {
            function.invoke(obj)
        } else {
            LoggerImpl.warning("Check cast: $obj")
        }
    }

    fun isOnStackTrace(clazz: String): Boolean {
        if (clazz.isBlank()) {
            return false
        }

        return Thread.currentThread().stackTrace.any { it.className.equals(clazz) }
    }
}
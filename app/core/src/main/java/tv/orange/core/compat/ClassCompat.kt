package tv.orange.core.compat

object ClassCompat {
    inline fun <reified T> Any.cast(): T {
        return this as T
    }
}
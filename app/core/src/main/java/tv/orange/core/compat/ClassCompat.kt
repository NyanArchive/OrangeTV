package tv.orange.core.compat

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
}
package tv.orange.core.models.flag

class Internal {
    interface ValueHolder {
        val value: Any
        val type: Type
    }

    enum class Type {
        BOOLEAN, INTEGER, STRING, LIST
    }

    interface Variant {
        fun getVariants(): List<Variant>
        fun default(): Variant
        fun fromString(value: String): Variant
        override fun toString(): String
    }

    @Suppress("UNCHECKED_CAST")
    class ListValue<T : Variant>(private val variant: T) : ValueHolder {
        var currentVariant: Variant? = variant.default()

        fun set(string: String): String {
            currentVariant = variant.fromString(string)

            return string
        }

        fun set(variant: Variant): String {
            currentVariant = variant

            return value
        }

        override val value: String
            get() = currentVariant.toString()

        override val type: Type
            get() = Type.LIST

        fun <T : Variant> getVariant(): T {
            return currentVariant as T
        }
    }

    class BooleanValue(bool: Any? = false) : ValueHolder {
        override var value = when (bool) {
            is Boolean -> bool
            is String -> bool.toBoolean()
            null -> false
            else -> bool.toString().toBoolean()
        }
        override val type: Type
            get() = Type.BOOLEAN
    }

    class IntegerValue(i: Any? = 0) : ValueHolder {
        override var value = when (i) {
            is Int -> i
            is String -> i.toInt()
            null -> 0
            else -> i.toString().toInt()
        }
        override val type: Type
            get() = Type.INTEGER
    }

    class StringValue(s: Any? = null) : ValueHolder {
        override var value = when (s) {
            is String -> s
            null -> ""
            else -> s.toString()
        }
        override val type: Type
            get() = Type.STRING
    }
}
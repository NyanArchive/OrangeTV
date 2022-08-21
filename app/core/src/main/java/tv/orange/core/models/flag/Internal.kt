package tv.orange.core.models.flag

class Internal {
    interface ValueHolder {
        val value: Any
    }

    interface Variant {
        fun getVariants(): List<Variant>
        fun getDefault(): Variant
        fun fromString(str: String): Variant?
        fun isDefault(): Boolean
        override fun toString(): String
    }

    class IntegerRangeValue(
        val minValue: Int,
        val maxValue: Int,
        private var currentValue: Int,
        val step: Int = 1
    ) : ValueHolder {

        fun getCurrentValue(): Int {
            return currentValue
        }

        fun setCurrentValue(value: Int) {
            currentValue = if (value > maxValue) {
                maxValue
            } else if (value < minValue) {
                minValue
            } else {
                value
            }
        }

        override val value: Any
            get() = currentValue
    }

    @Suppress("UNCHECKED_CAST")
    class ListValue<T : Variant>(private val variant: T) : ValueHolder {
        private var currentVariant: Variant = variant.getDefault()

        fun setCurrentVariant(value: String) {
            currentVariant = variant.fromString(str = value) ?: variant.getDefault()
        }

        fun getCurrentVariant(): Variant {
            return currentVariant
        }

        override val value: String
            get() = currentVariant.toString()

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
    }

    class IntegerValue(i: Any? = 0) : ValueHolder {
        override var value = when (i) {
            is Int -> i
            is String -> i.toInt()
            null -> 0
            else -> i.toString().toInt()
        }
    }

    class StringValue(s: Any? = null) : ValueHolder {
        override var value = when (s) {
            is String -> s
            null -> ""
            else -> s.toString()
        }
    }
}
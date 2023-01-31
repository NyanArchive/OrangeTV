package tv.orange.core.models.flag.core

class IntValue(i: Any? = 0) : ValueHolder {
    private var value = fromAny(i)

    override fun getValue(): Int {
        return value
    }

    override fun setValue(value: Any?) {
        this.value = fromAny(value)
    }

    companion object {
        fun fromAny(obj: Any?): Int {
            return when (obj) {
                is Int -> obj
                is String -> obj.toInt()
                null -> 0
                else -> obj.toString().toInt()
            }
        }
    }
}
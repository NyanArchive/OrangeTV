package tv.orange.core.models.flag.core

class StringValue(s: Any? = null) : ValueHolder {
    private var value: String = fromAny(s)

    override fun getValue(): String {
        return value
    }

    override fun setValue(value: Any?) {
        this.value = fromAny(value)
    }

    companion object {
        fun fromAny(obj: Any?): String {
            return when (obj) {
                is String -> obj
                null -> ""
                else -> obj.toString()
            }
        }
    }

}
package tv.orange.core.models.flag.core

class BooleanValue(bool: Any? = false) : ValueHolder {
    private var value = fromAny(bool)

    override fun getValue(): Boolean {
        return value
    }

    override fun setValue(value: Any?) {
        this.value = fromAny(value)
    }

    companion object {
        private fun fromAny(obj: Any?): Boolean {
            return when (obj) {
                is Boolean -> obj
                is String -> obj.toBoolean()
                null -> false
                else -> obj.toString().toBoolean()
            }
        }
    }
}
package tv.orange.core.models.flag.core

interface ValueHolder {
    fun getValue(): Any
    fun setValue(value: Any?)
}
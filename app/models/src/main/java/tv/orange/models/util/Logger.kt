package tv.orange.models.util

interface Logger {
    fun warning(msg: String)
    fun info(msg: String)
    fun debug(msg: String)
    fun rawDebug(msg: String)
    fun error(msg: String)
}
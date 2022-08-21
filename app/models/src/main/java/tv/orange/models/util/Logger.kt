package tv.orange.models.util

interface Logger {
    fun warning(messageText: String)
    fun info(messageText: String)
    fun debug(messageText: String)
    fun rawDebug(messageText: String)
    fun error(messageText: String)
}
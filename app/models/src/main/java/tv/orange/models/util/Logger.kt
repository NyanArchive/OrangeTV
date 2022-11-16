package tv.orange.models.util

interface Logger {
    fun warning(messageText: String?, includeStacktrace: Boolean)
    fun info(messageText: String?, includeStacktrace: Boolean)
    fun debug(messageText: String?, includeStacktrace: Boolean)
    fun rawDebug(messageText: String?, includeStacktrace: Boolean)
    fun error(messageText: String?, includeStacktrace: Boolean)
    fun devDebug(messageText: String?, includeStacktrace: Boolean)

    fun warning(messageText: String?)
    fun info(messageText: String?)
    fun debug(messageText: String?)
    fun rawDebug(messageText: String?)
    fun error(messageText: String?)
    fun devDebug(messageText: String?)

    fun devDebug(invoke: () -> String)
    fun devDebug(invoke: () -> String, includeStacktrace: Boolean)
}
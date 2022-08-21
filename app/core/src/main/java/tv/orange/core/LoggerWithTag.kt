package tv.orange.core

import tv.orange.models.util.Logger

class LoggerWithTag(private val tag: String) : Logger {
    override fun warning(messageText: String) {
        tv.orange.core.Logger.warning(messageText = "[$tag] $messageText")
    }

    override fun info(messageText: String) {
        tv.orange.core.Logger.info(messageText = "[$tag] $messageText")
    }

    override fun debug(messageText: String) {
        tv.orange.core.Logger.debug(messageText = "[$tag] $messageText")
    }

    override fun error(messageText: String) {
        tv.orange.core.Logger.error(messageText = "[$tag] $messageText")
    }

    override fun rawDebug(messageText: String) {
        tv.orange.core.Logger.rawDebug(messageText = "[$tag] $messageText")
    }
}
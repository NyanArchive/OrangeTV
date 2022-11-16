package tv.orange.core

import tv.orange.models.util.Logger


class LoggerWithTag(private val tag: String) : Logger {
    override fun warning(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.warning(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun warning(messageText: String?) {
        LoggerImpl.warning(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun info(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.info(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun info(messageText: String?) {
        LoggerImpl.info(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun debug(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.debug(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun debug(messageText: String?) {
        LoggerImpl.debug(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun rawDebug(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.rawDebug(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun rawDebug(messageText: String?) {
        LoggerImpl.rawDebug(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun error(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.error(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun error(messageText: String?) {
        LoggerImpl.error(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun devDebug(messageText: String?, includeStacktrace: Boolean) {
        LoggerImpl.devDebug(
            messageText = "[$tag] $messageText",
            includeStacktrace = includeStacktrace
        )
    }

    override fun devDebug(messageText: String?) {
        LoggerImpl.devDebug(
            messageText = "[$tag] $messageText",
            includeStacktrace = true
        )
    }

    override fun devDebug(invoke: () -> String) {
        LoggerImpl.devDebug(
            invoke = invoke,
            includeStacktrace = true
        )
    }

    override fun devDebug(invoke: () -> String, includeStacktrace: Boolean) {
        LoggerImpl.devDebug(
            invoke = invoke,
            includeStacktrace = includeStacktrace
        )
    }
}
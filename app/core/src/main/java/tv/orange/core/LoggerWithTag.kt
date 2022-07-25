package tv.orange.core

import tv.orange.models.Logger

class LoggerWithTag(private val tag: String) : Logger {
    override fun warning(msg: String) {
        tv.orange.core.Logger.warning("[$tag] $msg")
    }

    override fun info(msg: String) {
        tv.orange.core.Logger.info("[$tag] $msg")
    }

    override fun debug(msg: String) {
        tv.orange.core.Logger.debug("[$tag] $msg")
    }

    override fun rawDebug(msg: String) {
        tv.orange.core.Logger.rawDebug("[$tag] $msg")
    }

    override fun error(msg: String) {
        tv.orange.core.Logger.error("[$tag] $msg")
    }
}
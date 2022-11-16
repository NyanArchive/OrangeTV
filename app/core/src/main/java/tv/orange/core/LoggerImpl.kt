package tv.orange.core

import android.text.TextUtils
import android.util.Log
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.models.util.Logger

object LoggerImpl : Logger {
    private const val LOGGER_TAG = "PurpleTV"

    override fun warning(messageText: String?, includeStacktrace: Boolean) {
        Log.w(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = includeStacktrace
            )
        )
    }

    override fun warning(messageText: String?) {
        Log.w(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun info(messageText: String?, includeStacktrace: Boolean) {
        Log.i(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = includeStacktrace
            )
        )
    }

    override fun info(messageText: String?) {
        Log.i(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun debug(messageText: String?, includeStacktrace: Boolean) {
        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = includeStacktrace
            )
        )
    }

    override fun debug(messageText: String?) {
        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun rawDebug(messageText: String?, includeStacktrace: Boolean) {
        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun rawDebug(messageText: String?) {
        Log.d(
            LOGGER_TAG,
            "$messageText"
        )
    }

    override fun error(messageText: String?, includeStacktrace: Boolean) {
        Log.e(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = includeStacktrace
            )
        )
    }

    override fun error(messageText: String?) {
        Log.e(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun devDebug(messageText: String?, includeStacktrace: Boolean) {
        if (!Flag.DEV_MODE.asBoolean()) {
            return
        }

        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = includeStacktrace
            )
        )
    }

    override fun devDebug(messageText: String?) {
        if (!Flag.DEV_MODE.asBoolean()) {
            return
        }

        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = messageText,
                includeStacktrace = true
            )
        )
    }

    override fun devDebug(invoke: () -> String) {
        if (!Flag.DEV_MODE.asBoolean()) {
            return
        }

        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = invoke.invoke(),
                includeStacktrace = true
            )
        )
    }

    override fun devDebug(invoke: () -> String, includeStacktrace: Boolean) {
        if (!Flag.DEV_MODE.asBoolean()) {
            return
        }

        Log.d(
            LOGGER_TAG,
            buildMessage(
                messageText = invoke.invoke(),
                includeStacktrace = includeStacktrace
            )
        )
    }

    private fun buildMessage(
        messageText: String?,
        thread: Thread = Thread.currentThread(),
        includeStacktrace: Boolean = true,
        dropStackElements: Int = 4,
        takeStackElements: Int = 3
    ): String {
        return StringBuilder().apply {
            append("{\"message\": \"${messageText ?: "null"}\"")
            append(", \"thread\": \"${thread.name}\"")
            if (includeStacktrace) {
                val stack = TextUtils.join("->",
                    thread.stackTrace.drop(dropStackElements)
                        .take(takeStackElements)
                        .map { it.toString() })
                append(", \"stack\": \"$stack\"")
            }
            append("}")
        }.toString()
    }
}
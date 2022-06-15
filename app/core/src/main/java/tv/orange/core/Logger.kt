package tv.orange.core

import android.text.TextUtils
import android.util.Log

object Logger {
    private const val LOGGER_TAG = "TwitchMod"

    fun warning(msg: String) = Log.w(LOGGER_TAG, createMessage(Thread.currentThread(), msg))

    fun info(msg: String) = Log.i(LOGGER_TAG, createMessage(Thread.currentThread(), msg))

    fun debug(msg: String) = Log.d(LOGGER_TAG, createMessage(Thread.currentThread(), msg))

    fun rawDebug(msg: String) = Log.d(LOGGER_TAG, msg)

    fun error(msg: String) = Log.e(LOGGER_TAG, createMessage(Thread.currentThread(), msg))

    private fun createMessage(thread: Thread, message: String): String {
        val stack = TextUtils.join("->",
            thread.stackTrace.drop(4)
                .take(3)
                .map { it.toString() })

        return "{\"message\": \"$message\", \"thread\": \"${thread.name}\", \"stack\": \"$stack\"}"
    }
}
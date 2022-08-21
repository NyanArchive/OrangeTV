package tv.orange.core

import android.text.TextUtils
import android.util.Log
import tv.orange.models.util.Logger

object Logger : Logger {
    private const val LOGGER_TAG = "TwitchMod"

    override fun warning(messageText: String) {
        Log.w(LOGGER_TAG, createMessage(thread = Thread.currentThread(), messageText = messageText))
    }

    override fun info(messageText: String) {
        Log.i(LOGGER_TAG, createMessage(thread = Thread.currentThread(), messageText = messageText))
    }

    override fun debug(messageText: String) {
        Log.d(LOGGER_TAG, createMessage(thread = Thread.currentThread(), messageText = messageText))
    }

    override fun error(messageText: String) {
        Log.e(LOGGER_TAG, createMessage(thread = Thread.currentThread(), messageText = messageText))
    }

    override fun rawDebug(messageText: String) {
        Log.d(LOGGER_TAG, messageText)
    }

    private fun createMessage(thread: Thread, messageText: String): String {
        val stack = TextUtils.join("->",
            thread.stackTrace.drop(4)
                .take(3)
                .map { it.toString() })

        return "{\"message\": \"$messageText\", \"thread\": \"${thread.name}\", \"stack\": \"$stack\"}"
    }
}
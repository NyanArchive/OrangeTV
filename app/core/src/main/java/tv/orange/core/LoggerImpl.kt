package tv.orange.core

import android.text.TextUtils
import android.util.Log
import com.android.billingclient.api.BillingFlowParams
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.models.util.Logger
import tv.twitch.android.core.mvp.presenter.PresenterState
import tv.twitch.android.core.mvp.presenter.StateUpdateEvent

object LoggerImpl : Logger {
    private const val LOGGER_TAG = "PurpleTV"

    var devMode: Boolean = Flag.DEV_MODE.asBoolean()

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
        if (!devMode) {
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
        if (!devMode) {
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
        if (!devMode) {
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
        if (!devMode) {
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
        dropStackElements: Int = 5,
        takeStackElements: Int = 5
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

    fun debugStateUpdate(
        p0: String,
        p1: StateUpdateEvent,
        p2: PresenterState,
        p3: PresenterState,
        p4: MutableList<Any?>?
    ) {
        if (!devMode) {
            return
        }
        devDebug("$p0:new transition", false)
        devDebug("$p0:   event: $p1", false)
        devDebug("$p0:   previous state: $p2", false)
        devDebug("$p0:   new State: $p3", false)
        p4?.let {
            val iterator: Iterator<*> = it.iterator()
            while (iterator.hasNext()) {
                devDebug(p0 + ":   action: " + iterator.next(), false)
            }
        }
    }

    fun devDebug(params: BillingFlowParams) {
        devDebug("zze:${params.zze()}")
        devDebug("zze:${params.zzb()}")
        devDebug("zze:${params.zzc()}")
        devDebug("zze:${params.zze()}")
        devDebug("zze:${params.zzn()}")
    }
}
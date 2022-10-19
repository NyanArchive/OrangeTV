package tv.orange.features.tracking

import android.app.Application
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.core.SentryAndroidOptions
import io.sentry.protocol.User
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Logger
import java.lang.Integer.min

object SentrySDK {
    private var isInitialized: Boolean = false

    fun setupSentrySDK(application: Application) {
        val sentryDns = BuildConfigUtil.buildConfig.sentryDNS
        if (sentryDns.isNullOrBlank()) {
            Logger.error("Cannot setup SentrySDK: DNS not found")
            return
        }

        SentryAndroid.init(application) { options: SentryAndroidOptions ->
            options.dsn = sentryDns
            options.release = "$0.1+${BuildConfigUtil.buildConfig.number}"
            options.enableAllAutoBreadcrumbs(false)
            options.sampleRate = 1.0
            options.isEnableUserInteractionTracing = true
            options.isDebug = true
        }
        isInitialized = true
        Logger.debug("OK")
    }

    fun logException(th: Throwable?, context: String) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }

        th ?: run {
            Logger.warning("th is null")
            return
        }

        th.printStackTrace()
        addBreadcrumb(message = context, category = "SentrySDK::context")
        Sentry.captureException(th)
    }

    fun setTag(key: String?, value: String?) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }
        key ?: return

        val safeValue = value?.ifBlank { "empty" } ?: "empty"
        Sentry.setTag(key, safeValue.substring(0, min(safeValue.length, 32)))
    }

    private fun addBreadcrumb(message: String, category: String) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }

        Sentry.addBreadcrumb(message, category)
    }

    fun setBool(key: String?, z: Boolean) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }
        key ?: return

        Sentry.setTag(key, z.toString())
    }

    fun setUser(username: String?) {
        if (!isInitialized) {
            return
        }
        username ?: return

        Sentry.setUser(User().apply {
            setUsername(username)
        })
    }

    fun setInteger(key: String?, i: Int) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }
        key ?: return

        Sentry.setTag(key, i.toString())
    }

    fun setLong(key: String?, j: Long) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }
        key ?: return

        Sentry.setTag(key, j.toString())
    }

    fun logEvent(level: String, msg: String) {
        if (!isInitialized) {
            Logger.debug("called")
            return
        }

        Sentry.captureMessage(msg, level.toLevel())
    }
}

private fun String.toLevel(): SentryLevel {
    return when (this) {
        "VERBOSE" -> SentryLevel.DEBUG
        "DEBUG" -> SentryLevel.DEBUG
        "INFO" -> SentryLevel.INFO
        "WARN" -> SentryLevel.WARNING
        "ERROR" -> SentryLevel.ERROR
        "ASSERT" -> SentryLevel.FATAL
        else -> SentryLevel.DEBUG
    }
}

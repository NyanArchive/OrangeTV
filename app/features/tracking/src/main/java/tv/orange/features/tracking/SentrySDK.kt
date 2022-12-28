package tv.orange.features.tracking

import android.app.Application
import io.sentry.Sentry
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.core.SentryAndroidOptions
import io.sentry.protocol.User
import tv.orange.core.BuildConfigUtil
import tv.orange.core.LoggerImpl
import java.lang.Integer.min

object SentrySDK {
    private var isInitialized: Boolean = false

    fun setupSentrySDK(application: Application) {
        val sentryDns = BuildConfigUtil.buildConfig.sentryDNS
        if (sentryDns.isNullOrBlank()) {
            LoggerImpl.error("Cannot setup SentrySDK: DNS not found")
            return
        }

        val buildConfig = BuildConfigUtil.buildConfig
        SentryAndroid.init(application) { options: SentryAndroidOptions ->
            options.dsn = sentryDns
            options.release = "${buildConfig.getVersion()}+${buildConfig.number}"
            options.enableAllAutoBreadcrumbs(false)
            options.sampleRate = 1.0
        }
        isInitialized = true
        LoggerImpl.info("OK")
    }

    fun logException(th: Throwable?, context: String) {
        if (!isInitialized) {
            return
        }

        th ?: run {
            LoggerImpl.warning("th is null")
            return
        }

        th.printStackTrace()
        addBreadcrumb(message = context, category = "SentrySDK::context")
        Sentry.captureException(th)
    }

    fun setTag(key: String?, value: String?) {
        if (!isInitialized) {
            return
        }
        key ?: return

        val safeValue = value?.ifBlank { "empty" } ?: "empty"
        Sentry.setTag(key, safeValue.substring(0, min(safeValue.length, 32)))
    }

    private fun addBreadcrumb(message: String, category: String) {
        if (!isInitialized) {
            return
        }

        Sentry.addBreadcrumb(message, category)
    }

    fun setBool(key: String?, z: Boolean) {
        if (!isInitialized) {
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
            return
        }
        key ?: return

        Sentry.setTag(key, i.toString())
    }

    fun setLong(key: String?, j: Long) {
        if (!isInitialized) {
            return
        }
        key ?: return

        Sentry.setTag(key, j.toString())
    }

    fun logEvent(level: String, msg: String) {
        if (!isInitialized) {
            return
        }

        // Sentry.captureMessage(msg, level.toLevel())
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

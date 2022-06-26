package tv.orange.features.streamuptime

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.streamuptime.bridge.StreamUptimeView
import tv.orange.features.streamuptime.di.component.DaggerStreamUptimeComponent
import tv.orange.features.streamuptime.di.scope.StreamUptimeScope
import tv.twitch.android.models.streams.StreamModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@StreamUptimeScope
class Hook @Inject constructor() {
    fun bindStreamUptime(uptimeView: StreamUptimeView, streamModel: StreamModel) {
        val date = getStandardizeDateString(streamModel.createdAt) ?: run {
            Logger.error("Cannot parse createAt: ${streamModel.createdAt}")
            return
        }

        uptimeView.showUptime(calcStreamUptime(date))
    }

    companion object {
        private const val API_DATE_SUBSECOND_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        private const val API_DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"

        fun calcStreamUptime(created: Date): Int {
            return (Date().time - created.time).toInt() / 1000
        }

        fun getStandardizeDateString(str: String): Date? {
            return try {
                SimpleDateFormat(API_DATE_FORMAT_ISO8601, Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(str)
            } catch (e: ParseException) {
                try {
                    return SimpleDateFormat(API_DATE_SUBSECOND_FORMAT, Locale.getDefault()).apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                    }.parse(str)
                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
                null
            }
        }

        private val INSTANCE by lazy {
            val hook = DaggerStreamUptimeComponent.builder()
                .coreComponent(Core.getInjector().provideComponent(CoreComponent::class))
                .build().hook
            Logger.debug("created: $hook")

            return@lazy hook
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }
    }
}
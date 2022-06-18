package tv.orange.features.streamuptime

import tv.orange.core.Logger
import tv.orange.features.streamuptime.bridge.StreamUptimeView
import tv.twitch.android.models.streams.StreamModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Hook {
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

        val instance by lazy {
            Hook()
        }
    }
}
package tv.orange.core

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val API_DATE_SUBSECOND_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val API_DATE_FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"

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
}
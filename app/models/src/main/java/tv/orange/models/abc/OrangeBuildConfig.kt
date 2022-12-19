package tv.orange.models.abc

import java.util.*

data class OrangeBuildConfig(
    val number: Int = 0,
    val timestamp: Int = 0,
    val version: Int = 0,
    val sentryDNS: String? = null,
    val codename: String = "Orange"
) {
    fun timestampToDate(): Date? {
        if (timestamp <= 0) {
            return null
        }

        return Date(timestamp.toLong() * 1000)
    }

    fun getVersion(): String {
        return "${version.div(100)}.${version % 100}"
    }
}

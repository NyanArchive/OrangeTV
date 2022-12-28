package tv.orange.core

import org.json.JSONObject
import tv.orange.models.abc.OrangeBuildConfig
import tv.twitch.android.app.core.ApplicationContext
import java.util.*

object BuildConfigUtil {
    private const val BUILD_JSON_FILENAME = "build.json"
    private const val USER_AGENT_TEMPLATE = "PurpleTV/%s Build/%d"

    val userAgent by lazy {
        String.format(
            Locale.ENGLISH,
            USER_AGENT_TEMPLATE,
            buildConfig.getVersion(),
            buildConfig.number
        )
    }

    val buildConfig: OrangeBuildConfig by lazy {
        return@lazy try {
            with(JSONObject(getRawBuildString())) {
                OrangeBuildConfig(
                    number = getInt("number"),
                    version = getInt("version"),
                    timestamp = getInt("timestamp"),
                    sentryDNS = getString("sentryDNS"),
                    codename = getString("codename")
                )
            }
        } catch (e: Throwable) {
            OrangeBuildConfig()
        }
    }

    private fun getRawBuildString(): String {
        val context = ApplicationContext.getInstance().context

        return context.assets.open(BUILD_JSON_FILENAME).bufferedReader().use {
            it.readText()
        }
    }
}
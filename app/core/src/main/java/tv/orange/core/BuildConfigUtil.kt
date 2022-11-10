package tv.orange.core

import org.json.JSONObject
import tv.orange.models.abc.OrangeBuildConfig
import tv.twitch.android.app.core.ApplicationContext

object BuildConfigUtil {
    const val BUILD_JSON_FILENAME = "build.json"
    const val USER_AGENT_TEMPLATE = "PurpleTV/0.1 Build/%d"

    val buildConfig: OrangeBuildConfig by lazy {
        return@lazy try {
            with(JSONObject(getRawBuildString())) {
                OrangeBuildConfig(
                    number = getInt("number"),
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
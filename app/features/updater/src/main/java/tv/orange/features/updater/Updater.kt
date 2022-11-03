package tv.orange.features.updater

import android.content.Context
import android.graphics.Color
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.features.updater.data.view.UpdaterActivity
import tv.orange.models.abc.Feature
import javax.inject.Inject

class Updater @Inject constructor() : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Updater::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(Updater::class.java)
        }
    }

    fun createUpdaterFragment(context: Context) {
        UpdaterActivity.startActivity(
            context = context,
            codename = "Orange",
            url = "https://nopbreak.ru/shared/twitchmod/release/3398.apk",
            logoUrl = null,
            build = BuildConfigUtil.buildConfig.number,
            changelog = "Added: more bugz\n".repeat(5),
            color = ResourceManager.get().getColor("orange"),
        )
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
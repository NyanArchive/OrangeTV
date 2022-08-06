package tv.orange.features.ui

import tv.orange.core.Core
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.ui.di.scope.UIScope
import tv.orange.models.abc.Feature
import javax.inject.Inject

@UIScope
class UI @Inject constructor() : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(UI::class.java)

        @JvmStatic
        val hideLeaderboards: Boolean
            get() = Flag.HIDE_LEADERBOARDS.asBoolean()
    }

    val skipTwitchBrowserDialog: Boolean
        get() = Flag.DISABLE_LINK_DISCLAIMER.asBoolean()

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
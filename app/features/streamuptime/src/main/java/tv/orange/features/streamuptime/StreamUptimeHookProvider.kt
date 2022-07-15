package tv.orange.features.streamuptime

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.util.DateUtil
import tv.orange.features.streamuptime.bridge.StreamUptimeView
import tv.orange.features.streamuptime.di.component.DaggerStreamUptimeComponent
import tv.orange.features.streamuptime.di.scope.StreamUptimeScope
import tv.twitch.android.models.streams.StreamModel
import java.util.*
import javax.inject.Inject

@StreamUptimeScope
class StreamUptimeHookProvider @Inject constructor() {
    fun bindStreamUptime(uptimeView: StreamUptimeView, streamModel: StreamModel) {
        val date = DateUtil.getStandardizeDateString(streamModel.createdAt) ?: run {
            Logger.error("Can't parse: ${streamModel.createdAt}")
            return
        }

        uptimeView.showUptime(calcStreamUptime(date))
    }

    companion object {
        private fun calcStreamUptime(created: Date): Int {
            return (Date().time - created.time).toInt() / 1000
        }

        private val INSTANCE by lazy {
            val hook = DaggerStreamUptimeComponent.builder()
                .coreComponent(Core.getInjector().provideComponent(CoreComponent::class))
                .build().hook

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): StreamUptimeHookProvider {
            return INSTANCE
        }
    }
}
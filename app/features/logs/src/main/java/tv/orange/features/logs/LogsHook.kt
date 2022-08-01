package tv.orange.features.logs

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.logs.di.component.DaggerLogsComponent
import tv.orange.features.logs.di.component.LogsComponent
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.features.logs.view.ViewFactory
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import tv.twitch.android.shared.chat.moderation.ModerationActionBottomSheetViewDelegate
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel
import javax.inject.Inject

@LogsScope
class LogsHook @Inject constructor(val viewFactory: ViewFactory) {
    private lateinit var logsComponent: LogsComponent

    fun createModLogsButton(state: ModerationBottomSheetViewState): BottomSheetListItemModel<*> {
        return viewFactory.createModLogsButton(state)
    }

    fun showModLogs(
        activity: FragmentActivity,
        event: ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent
    ): Boolean {
        if (event is ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs) {
            val fragment = logsComponent.modLogsFragment
            fragment.bind(activity)
            fragment.show(activity.supportFragmentManager, "mod_logs")
            fragment.load(event.userId, event.channelId)
            return true
        }

        return false
    }

    fun injectModLogsButton(
        list: List<BottomSheetListItemModel<*>>,
        state: ModerationBottomSheetViewState
    ): List<BottomSheetListItemModel<*>> {
        return list.toMutableList().apply {
            add(createModLogsButton(state))
        }
    }

    companion object {
        private val INSTANCE by lazy {
            val component = DaggerLogsComponent.factory().create(
                Core.getProvider(CoreComponent::class).get(),
                Core.getProvider(ChatMessageFactory.Factory::class).get(),
                Core.getProvider(GraphQlService::class).get()
            )

            val instance = component.hook
            component.hook.logsComponent = component

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): LogsHook {
            return INSTANCE
        }
    }
}
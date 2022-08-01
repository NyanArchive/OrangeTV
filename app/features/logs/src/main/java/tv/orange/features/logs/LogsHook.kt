package tv.orange.features.logs

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
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
    fun showModLogs(
        activity: FragmentActivity,
        event: ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent
    ): Boolean {
        if (event is ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs) {
            val fragment = COMPONENT!!.modLogsFragment
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
            add(viewFactory.createModLogsButton(state))
        }
    }

    companion object {
        private var COMPONENT: LogsComponent? = null
        private var INSTANCE: LogsHook? = null

        private fun buildComponent(): LogsComponent {
            return DaggerLogsComponent.factory().create(
                Core.getProvider(CoreComponent::class).get(),
                Core.getProvider(ChatMessageFactory.Factory::class).get(),
                Core.getProvider(GraphQlService::class).get()
            ).apply {
                COMPONENT = this
            }
        }

        private fun buildInstance(): LogsHook {
            return COMPONENT!!.hook.apply {
                INSTANCE = this
            }
        }

        @JvmStatic
        fun get(): LogsHook {
            INSTANCE?.let {
                return it
            }

            synchronized(this) {
                COMPONENT ?: buildComponent()
                INSTANCE ?: buildInstance()

                return INSTANCE!!
            }
        }

        fun clear() {
            COMPONENT = null
            INSTANCE = null
        }
    }
}
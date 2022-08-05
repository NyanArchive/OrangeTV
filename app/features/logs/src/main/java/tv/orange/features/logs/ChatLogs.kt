package tv.orange.features.logs

import androidx.fragment.app.FragmentActivity
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.features.logs.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.chat.moderation.ModerationActionBottomSheetViewDelegate
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel
import javax.inject.Inject

@LogsScope
class ChatLogs @Inject constructor(
    val viewFactory: ViewFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ChatLogs::class.java)
    }

    fun showModLogs(
        activity: FragmentActivity,
        event: ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent
    ): Boolean {
        if (event is ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs) {
            val fragment = viewFactory.createLogsFragment()
            fragment.bind(activity)
            fragment.show(activity.supportFragmentManager, "orange_logs")
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

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
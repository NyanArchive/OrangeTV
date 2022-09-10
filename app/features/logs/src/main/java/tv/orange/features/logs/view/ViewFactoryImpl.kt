package tv.orange.features.logs.view

import tv.orange.core.ResourceManager
import tv.orange.features.logs.component.data.repository.LogsRepository
import tv.orange.features.logs.data.adapter.LogsAdapter
import tv.orange.features.logs.data.view.LogsFragment
import tv.twitch.android.shared.chat.moderation.ModerationActionBottomSheetViewDelegate
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor(
    val logsRepository: LogsRepository,
    val logsAdapter: LogsAdapter
) : ViewFactory {
    override fun createModLogsButton(state: ModerationBottomSheetViewState): BottomSheetListItemModel<*> {
        return BottomSheetListItemModel(
            ResourceManager.get().getId(resName = "chat_moderation_menu__mod_logs"),
            ResourceManager.get().getString(resName = "orange_mod_logs"),
            true,
            ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs(
                state.username,
                state.channelId
            )
        )
    }

    override fun createLogsFragment(): LogsFragment {
        return LogsFragment(logsRepository, logsAdapter)
    }
}
package tv.orange.features.logs.view

import tv.orange.core.ResourceManager
import tv.twitch.android.shared.chat.moderation.ModerationActionBottomSheetViewDelegate
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createModLogsButton(state: ModerationBottomSheetViewState): BottomSheetListItemModel<*> {
        return BottomSheetListItemModel(
            ResourceManager.getResId("chat_moderation_menu__mod_logs"),
            ResourceManager.getString("orange_mod_logs"),
            true,
            ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs(
                state.username,
                state.channelId
            )
        )
    }
}
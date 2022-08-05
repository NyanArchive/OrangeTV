package tv.orange.features.logs.view

import tv.orange.features.logs.data.view.LogsFragment
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel

interface ViewFactory {
    fun createModLogsButton(state: ModerationBottomSheetViewState): BottomSheetListItemModel<*>
    fun createLogsFragment(): LogsFragment
}
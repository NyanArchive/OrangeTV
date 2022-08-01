package tv.twitch.android.shared.chat.moderation;

import java.util.List;

import tv.orange.features.logs.LogsHook;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel;

public class ModerationActionBottomSheetViewDelegate {
    /* ... */

    public void render(ModerationBottomSheetViewState state) {
        /* ... */

        List<BottomSheetListItemModel<?>> list = null;

        list = LogsHook.get().injectModLogsButton(list, state); // TODO: __INJECT_CODE

        //bottomSheetListViewDelegate.configureItems(CollectionsKt.listOf((Object[]) bottomSheetListItemModelArr));

        throw new VirtualImpl();
    }

    public static abstract class ModerationActionButtonEvent implements ViewDelegateEvent {}

    /* ... */
}
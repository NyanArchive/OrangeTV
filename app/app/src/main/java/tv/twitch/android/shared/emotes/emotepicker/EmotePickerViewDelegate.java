package tv.twitch.android.shared.emotes.emotepicker;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.chat.bridge.ScrollableOrangeSection;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.adapters.RecyclerAdapterSection;
import tv.twitch.android.core.adapters.TwitchSectionAdapter;
import tv.twitch.android.shared.emotes.emotepicker.adapter.EmotePickerAdapterSection;
import tv.twitch.android.shared.emotes.emotepicker.models.EmotePickerSection;
import tv.twitch.android.shared.ui.elements.list.ContentListViewDelegate;

public class EmotePickerViewDelegate implements ScrollableOrangeSection { // TODO: __IMPLEMENT
    private final ContentListViewDelegate listViewDelegate = null;
    private final ImageView orangeEmotesButton; // TODO: __INJECT_FIELD

    public EmotePickerViewDelegate() {

        /* ... */

        orangeEmotesButton = ChatHookProvider.get().getOrangeEmotesButton(this); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public void scrollToOrangeSection() { // TODO: __INJECT_METHOD
        if (listViewDelegate == null) {
            return;
        }

        TwitchSectionAdapter adapter = (TwitchSectionAdapter) listViewDelegate.getAdapter();

        if (adapter == null) {
            return;
        }

        List<RecyclerAdapterSection> sections = adapter.getSections();
        if (sections == null || sections.size() == 0) {
            return;
        }

        int pos = 0;

        int sizeWithHeader = 0;
        for (RecyclerAdapterSection t : sections) {
            EmotePickerAdapterSection section = (EmotePickerAdapterSection) t;

            if (section.getEmotePickerSection() == EmotePickerSection.ORANGE) {
                pos = sizeWithHeader;
                break;
            }

            sizeWithHeader += section.sizeWithHeader();
        }

        listViewDelegate.fastScrollToPosition(pos);
    }

    public void render(EmotePickerPresenter.EmotePickerState state) {
        ChatHookProvider.get().renderEmotePickerState(state, orangeEmotesButton); // TODO: __INJECT_CODE
    }

    public static final class Companion {
        public final EmotePickerViewDelegate create(Context context, ViewGroup.LayoutParams layoutParams, Object config) {
            if (ChatHookProvider.enableStickyHeaders()) {
                // uContentList.enableStickyHeaders();
            }

            throw new VirtualImpl();
        }
    }
}
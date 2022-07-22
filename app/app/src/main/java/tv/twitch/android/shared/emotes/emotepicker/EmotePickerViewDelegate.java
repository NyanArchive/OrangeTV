package tv.twitch.android.shared.emotes.emotepicker;

import android.content.Context;
import android.view.ViewGroup;

import tv.orange.features.chat.ChatHookProvider;

public class EmotePickerViewDelegate {
    public static final class Companion {
        public final EmotePickerViewDelegate create(Context context, ViewGroup.LayoutParams layoutParams, Object config) {
            if (ChatHookProvider.enableStickyHeaders()) {
                // uContentList.enableStickyHeaders();
            }

            return null;
        }
    }
}
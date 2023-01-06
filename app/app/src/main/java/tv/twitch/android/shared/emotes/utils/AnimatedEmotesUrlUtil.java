package tv.twitch.android.shared.emotes.utils;

import tv.twitch.android.shared.chat.settings.preferences.ChatSettingsPreferencesFile;

public class AnimatedEmotesUrlUtil {
    private ChatSettingsPreferencesFile chatSettingsPreferencesFile;

    /* ... */

    public boolean isAnimatedEmotesEnabled() { // TODO: __INJECT_METHOD
        return chatSettingsPreferencesFile.isAnimatedEmotesEnabled();
    }
}

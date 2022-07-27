package tv.twitch.android.shared.chat.settings.entry;

import tv.orange.features.chat.bridge.ChatSettingsOrangeEvents;

public class ChatSettingsPresenter$handleViewEvents$1 {
    ChatSettingsPresenter this$0;

    public final void invoke(ChatSettingsViewDelegate.ChatSettingsEvents event) {
        String z;
        if (event instanceof ChatSettingsOrangeEvents.Closable) { // TODO: __INJECT_CODE
            z = "A";
            // this.this$0.close();
        } else {
            z = "B";
        }
        z.equals(z);
    }
}

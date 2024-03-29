package tv.twitch.android.shared.ui.elements.span;

import tv.orange.features.chat.ChatHookProvider;

public enum MediaSpan$Type {
    AnimatedBit(24.0f),
    Emote(24.0f),
    MediumEmote(20.0f),
    SmallEmote(14.0f),
    Badge(18.0f),
    Reward(18.0f);

    private final float sizeDp;

    MediaSpan$Type(float f2) {
        this.sizeDp = f2;
    }

    public final float getSizeDp() { // TODO: __REPLACE_METHOD
        return ChatHookProvider.hookMediaSpanSizeDp(this.sizeDp);
    }
}

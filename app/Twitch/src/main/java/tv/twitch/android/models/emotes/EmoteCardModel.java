package tv.twitch.android.models.emotes;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class EmoteCardModel {
    private EmoteModelAssetType assetType;
    private EmoteCardType emoteCardType;
    private String emoteId;
    private String emoteToken;

    public EmoteCardModel(String str, String str2, EmoteModelAssetType emoteModelAssetType, EmoteCardType emoteCardType, DefaultConstructorMarker defaultConstructorMarker) {
        throw new AbstractMethodError();
    }
    public String getEmoteId() {
        return this.emoteId;
    }

    public String getEmoteToken() {
        return this.emoteToken;
    }

    public EmoteModelAssetType getAssetType() {
        return this.assetType;
    }

    public EmoteCardType getEmoteCardType() {
        return this.emoteCardType;
    }
}

package tv.twitch.android.models.emotes;

import java.util.List;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class EmoteSet {
    public EmoteSet(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract List<EmoteModel> getEmotes();

    public abstract String getSetId();

    private EmoteSet() {
    }
}

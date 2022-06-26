package tv.twitch.android.models.chat;

public class MessageBadge { // TODO: __REMOVE_FINAL
    private final String name;
    private final String version;

    public MessageBadge(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public final String getName() {
        return this.name;
    }

    public final String getVersion() {
        return this.version;
    }
}

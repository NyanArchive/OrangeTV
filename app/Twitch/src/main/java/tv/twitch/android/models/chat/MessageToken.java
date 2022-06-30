package tv.twitch.android.models.chat;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class MessageToken {
    public MessageToken(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public static final class BitsToken extends MessageToken {
        private final int numBits;
        private final String prefix;

        public BitsToken(String prefix, int i) {
            super(null);
            this.prefix = prefix;
            this.numBits = i;
        }

        public final int getNumBits() {
            return this.numBits;
        }

        public final String getPrefix() {
            return this.prefix;
        }
    }

    private MessageToken() {
    }

    public static final class EmoticonToken extends MessageToken {
        private final String id;
        private final String text;

        public EmoticonToken(String text, String id) {
            super(null);
            this.text = text;
            this.id = id;
        }

        public final String getId() {
            return this.id;
        }

        public final String getText() {
            return this.text;
        }
    }

    public static final class MentionToken extends MessageToken {
        private final boolean isLocalUser;
        private final String text;
        private final String userName;

        public MentionToken(String text, String userName, boolean z) {
            super(null);
            this.text = text;
            this.userName = userName;
            this.isLocalUser = z;
        }

        public final String getText() {
            return this.text;
        }

        public final String getUserName() {
            return this.userName;
        }

        public final boolean isLocalUser() {
            return this.isLocalUser;
        }
    }

    public static final class TextToken extends MessageToken {
        private final AutoModMessageFlags flags;
        private final String text;

        public TextToken(String text, AutoModMessageFlags flags) {
            super(null);
            this.text = text;
            this.flags = flags;
        }

        public final AutoModMessageFlags getFlags() {
            return this.flags;
        }

        public final String getText() {
            return this.text;
        }
    }

    public static final class UrlToken extends MessageToken {
        private final boolean hidden;
        private final String url;

        public UrlToken(String url, boolean z) {
            super(null);
            this.url = url;
            this.hidden = z;
        }

        public final boolean getHidden() {
            return this.hidden;
        }

        public final String getUrl() {
            return this.url;
        }
    }
}

package tv.twitch.android.models.emotes;

import java.util.List;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class EmoteModel {
    public EmoteModel(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract EmoteModelAssetType getAssetType();

    public abstract String getId();

    public abstract String getToken();

    public abstract EmoteModelType getType();

    private EmoteModel() {
    }

    public static class Generic extends EmoteModel {
        public Generic(String var1, String var2, EmoteModelAssetType var3, EmoteModelType var4) {
            /* ... */
        }

        @Override
        public EmoteModelAssetType getAssetType() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getToken() {
            return null;
        }

        @Override
        public EmoteModelType getType() {
            return null;
        }
    }

    public static class WithOwner extends EmoteModel {
        public WithOwner(String var1, String var2, int var3, EmoteModelAssetType var4, List<? extends EmoteModel> var5, EmoteModelType var6) {
            /* ... */
        }

        @Override
        public EmoteModelAssetType getAssetType() {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getToken() {
            return null;
        }

        @Override
        public EmoteModelType getType() {
            return null;
        }
    }
}

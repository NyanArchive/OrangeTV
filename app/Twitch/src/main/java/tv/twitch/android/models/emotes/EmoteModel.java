package tv.twitch.android.models.emotes;

import java.util.List;

public abstract class EmoteModel {
    public static class Generic extends EmoteModel {
        public Generic(String var1, String var2, EmoteModelAssetType var3, EmoteModelType var4) {
            /* ... */
        }
    }

    public static class WithOwner extends EmoteModel {
        public WithOwner(String var1, String var2, int var3, EmoteModelAssetType var4, List<? extends EmoteModel> var5, EmoteModelType var6) {
            /* ... */
        }
    }
}

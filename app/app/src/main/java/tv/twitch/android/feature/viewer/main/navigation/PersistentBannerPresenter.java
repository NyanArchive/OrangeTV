package tv.twitch.android.feature.viewer.main.navigation;

import tv.orange.features.updater.Updater;

public class PersistentBannerPresenter {
    /* ... */

    public interface PersistentBannerPresenterListener {
        void installBannerShown();

        void installUpdate();

        void updateDismissed();
    }

    /* ... */

    public static final class PersistentBanners {
        static {
            int app_update_available = Updater.getOrangeAppUpdateAvailable(); // TODO: __REPLACE_CODE
            int app_update_available_cta = Updater.getOrangeAppUpdateAvailableCta(); // TODO __REPLACE_CODE
        }
    }

    /* ... */
}
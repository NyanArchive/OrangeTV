package tv.twitch.android.app.consumer.dagger;

import java.util.Map;

import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.internal.MapBuilder;
import tv.orange.features.settings.OrangeSettings;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.consumer.TwitchApplication;

public class DaggerAppComponent {
    private static final class AppComponentImpl implements AppComponent {
        @Override
        public void inject(TwitchApplication twitchApplication) {
            throw new VirtualImpl();
        }
    }
    /* ... */

    private static final class SettingsActivitySubcomponentImpl {
        private SettingsActivitySubcomponentImpl settingsActivitySubcomponentImpl;
        private DaggerAppComponent.AppComponentImpl appComponentImpl;

        /* ... */

        private Map<Class<?>, Provider<AndroidInjector.Factory<?>>> mapOfClassOfAndProviderOfAndroidInjectorFactoryOf() {
            /* ... */

            MapBuilder builder = null;

            /* ... */
            OrangeSettings.get().inject(builder, settingsActivitySubcomponentImpl, appComponentImpl); // TODO: __INJECT_CODE

            /* ... */

            throw new VirtualImpl();
        }
    }

    /* ... */
}

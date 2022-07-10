package tv.twitch.android.app.consumer.dagger;

import java.util.Map;

import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.internal.MapBuilder;
import tv.orange.features.settings.OrangeSettings;

public class DaggerAppComponent {
    private static final class SettingsActivitySubcomponentImpl {
        private SettingsActivitySubcomponentImpl settingsActivitySubcomponentImpl;

        private Map<Class<?>, Provider<AndroidInjector.Factory<?>>> mapOfClassOfAndProviderOfAndroidInjectorFactoryOf() {
            MapBuilder builder = null;
            OrangeSettings.get().inject(builder, settingsActivitySubcomponentImpl); // TODO: __INJECT_CODE

            return null;
        }

    }
}

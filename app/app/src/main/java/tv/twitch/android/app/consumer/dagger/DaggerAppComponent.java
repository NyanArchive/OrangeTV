package tv.twitch.android.app.consumer.dagger;

import androidx.work.ListenableWorker;

import java.util.Collections;
import java.util.Map;

import javax.inject.Provider;

import dagger.android.AndroidInjector;
import dagger.internal.MapBuilder;
import tv.orange.features.settings.OrangeSettings;
import tv.orange.features.updater.bridge.DInjector;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.consumer.TwitchApplication;
import tv.twitch.android.core.work.AssistedWorkerFactory;
import tv.twitch.android.feature.tablet.homeshelf.HomeMediaRowWorker;

public class DaggerAppComponent {
    private static final class AppComponentImpl implements AppComponent {
        private Provider<HomeMediaRowWorker.Factory> factoryProvider2;

        /* ... */

        @Override
        public void inject(TwitchApplication twitchApplication) {
            throw new VirtualImpl();
        }

        private Map<Class<? extends ListenableWorker>, Provider<AssistedWorkerFactory<? extends ListenableWorker>>> mapOfClassOfAndProviderOfAssistedWorkerFactoryOf() {
            return DInjector.injectDownloadUpdateWorker(Collections.emptyMap()); // TODO: __INJECT_MAP
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

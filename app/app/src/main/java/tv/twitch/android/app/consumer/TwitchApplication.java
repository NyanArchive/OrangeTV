package tv.twitch.android.app.consumer;

import android.app.Application;

import androidx.annotation.NonNull;

import tv.orange.core.Core;
import tv.orange.core.PreferenceManager;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.stv.AvatarsHookProvider;
import tv.orange.injector.Injector;
import tv.orange.models.InjectorProvider;
import tv.twitch.android.app.consumer.dagger.AppComponent;
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent;

public class TwitchApplication extends Application implements InjectorProvider {
    private volatile Injector injector = null; // TODO: __ADD_FIELD

    public void onCreate() {
        super.onCreate();

        /* ... */

        // Inject after twitch dagger
        AppComponent appComponent = createDaggerComponent(); // TODO: __INJECT_CODE
        appComponent.inject(this); // TODO: __INJECT_CODE
        injector = new Injector((DaggerAppComponent) appComponent); // TODO: __INJECT_CODE
        Core.Companion.initialize(this); // TODO: __INJECT_CODE
        initOranges(); // TODO: __INJECT_CODE

        /* ... */
    }

    private void initOranges() { // TODO: __INJECT_METHOD
        ChatHookProvider.get().registerLifecycle(Core.get(), PreferenceManager.get());
        Core.get().registerLifecycleListener(AvatarsHookProvider.get());
    }

    protected AppComponent createDaggerComponent() {
        return AppComponent.Initializer.build();
    }

    @NonNull
    @Override
    public Injector provideInjector() { // TODO: __INJECT_METHOD
        return injector;
    }
}
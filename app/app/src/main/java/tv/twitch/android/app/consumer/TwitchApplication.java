package tv.twitch.android.app.consumer;

import android.app.Application;

import androidx.annotation.NonNull;

import tv.orange.core.Core;
import tv.orange.core.PreferenceManager;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.features.stv.AvatarsHookProvider;
import tv.orange.injector.Injector;
import tv.orange.models.InjectorProvider;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.app.consumer.dagger.AppComponent;
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent;

public class TwitchApplication extends Application implements InjectorProvider {
    private volatile Injector injector = null; // TODO: __ADD_FIELD

    /* ... */

    public void onCreate() {
        super.onCreate();

        /* ... */

        AppComponent appComponent = createDaggerComponent(); // TODO: __INJECT_CODE
        appComponent.inject(this); // TODO: __INJECT_CODE
        initOranges(appComponent); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    private void initOranges(AppComponent appComponent) { // TODO: __INJECT_METHOD
        injector = Injector.create((DaggerAppComponent) appComponent);
        Core.Companion.initialize(this);
        PreferenceManager prefManager = PreferenceManager.get();
        Core core = Core.get();
        AvatarsHookProvider avatarsHookProvider = AvatarsHookProvider.get();

        ChatHookProvider.get().registerLifecycle(core, prefManager);

        core.registerLifecycleListeners(avatarsHookProvider);
        prefManager.registerFlagListeners(avatarsHookProvider);
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
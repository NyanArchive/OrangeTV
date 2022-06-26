package tv.twitch.android.app.consumer;

import android.app.Application;

import androidx.annotation.NonNull;

import tv.orange.core.Core;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.injector.Injector;
import tv.orange.models.InjectorProvider;

public class TwitchApplication extends Application implements InjectorProvider {
    private volatile Injector injector = null; // TODO: __ADD_FIELD

    public void onCreate() {
        super.onCreate();

        /* ... */

        // Inject after twitch dagger
        injector = new Injector(); // TODO: __INJECT_CODE
        Core.Companion.initialize(this); // TODO: __INJECT_CODE
        initOranges(); // TODO: __INJECT_CODE

        /* ... */
    }

    private void initOranges() { // TODO: __INJECT_METHOD
        ChatHookProvider.get().registerLifecycle(Core.get());
    }

    @NonNull
    @Override
    public Injector provideInjector() { // TODO: __INJECT_METHOD
        return injector;
    }
}
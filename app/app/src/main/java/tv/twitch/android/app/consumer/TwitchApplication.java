package tv.twitch.android.app.consumer;

import android.app.Application;

import androidx.annotation.NonNull;

import tv.orange.bridge.di.Bridge;
import tv.orange.injector.Injector;
import tv.orange.models.BridgeProvider;
import tv.orange.models.InjectorProvider;
import tv.orange.models.VirtualImpl;
import tv.twitch.android.app.consumer.dagger.AppComponent;
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent;

public class TwitchApplication extends Application implements InjectorProvider, BridgeProvider { // TODO: __IMPLEMENT
    private volatile Injector injector = null; // TODO: __ADD_FIELD
    private volatile Bridge bridge = null; // TODO: __ADD_FIELD

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
        bridge = Bridge.create();
        bridge.initialize();
    }

    protected AppComponent createDaggerComponent() {
        return AppComponent.Initializer.build();
    }

    @NonNull
    @Override
    public Injector provideInjector() { // TODO: __INJECT_METHOD
        return injector;
    }

    @NonNull
    @Override
    public tv.orange.models.Bridge provideBridge() { // TODO: __INJECT_METHOD
        return bridge;
    }
}
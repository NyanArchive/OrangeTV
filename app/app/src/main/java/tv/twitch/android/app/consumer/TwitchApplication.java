package tv.twitch.android.app.consumer;

import android.app.Application;

import androidx.annotation.NonNull;

import tv.orange.bridge.TwitchComponentProviderImpl;
import tv.orange.bridge.di.BridgeImpl;
import tv.orange.core.Core;
import tv.orange.models.abc.Bridge;
import tv.orange.models.abc.BridgeProvider;
import tv.orange.models.abc.TCPProvider;
import tv.orange.models.abc.TwitchComponentProvider;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.consumer.dagger.AppComponent;
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent;

public class TwitchApplication extends Application implements TCPProvider, BridgeProvider { // TODO: __IMPLEMENT
    private volatile TwitchComponentProvider twitchComponentProvider = null; // TODO: __ADD_FIELD
    private volatile Bridge orangeBridge = null; // TODO: __ADD_FIELD

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
        twitchComponentProvider = TwitchComponentProviderImpl.create();
        ((TwitchComponentProviderImpl) twitchComponentProvider).initialize((DaggerAppComponent) appComponent);
        orangeBridge = BridgeImpl.create();
        ((BridgeImpl) orangeBridge).initialize(this);
        Core.setBridge(orangeBridge);
        ((BridgeImpl) orangeBridge).initializeFeatures();
    }

    protected AppComponent createDaggerComponent() {
        return AppComponent.Initializer.build();
    }

    @NonNull
    @Override
    public TwitchComponentProvider provideTCP() { // TODO: __INJECT_METHOD
        return twitchComponentProvider;
    }

    @NonNull
    @Override
    public Bridge provideBridge() { // TODO: __INJECT_METHOD
        return orangeBridge;
    }
}
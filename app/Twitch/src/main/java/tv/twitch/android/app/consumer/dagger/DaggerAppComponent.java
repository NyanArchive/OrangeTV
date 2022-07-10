package tv.twitch.android.app.consumer.dagger;

import androidx.fragment.app.FragmentActivity;

import javax.inject.Provider;

import tv.twitch.android.settings.base.SettingsTracker;
import tv.twitch.android.shared.ui.menus.core.MenuAdapterBinder;

public class DaggerAppComponent {
    public static final class SettingsActivitySubcomponentImpl {
        private Provider<SettingsTracker> provideSettingsTrackerProvider;
        private Provider<MenuAdapterBinder> provideMenuAdapterBinderProvider;
        private Provider<FragmentActivity> provideFragmentActivityProvider;
    }
}

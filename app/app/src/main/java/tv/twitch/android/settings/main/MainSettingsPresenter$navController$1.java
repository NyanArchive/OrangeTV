package tv.twitch.android.settings.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import tv.orange.features.settings.OrangeSettings;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.settings.SettingsDestination;

public class MainSettingsPresenter$navController$1 {
    /* ... */

    public void navigateToSettingFragment(SettingsDestination settingsDestination, Bundle bundle) {
        Fragment accountSettingsFragment = null;

        /* ... */

        switch (6) {
            case 6:
                accountSettingsFragment = OrangeSettings.get().createSettingsFragment(); // TODO: __REPLACE_CODE
                break;
        }

        accountSettingsFragment.getActivity();

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */
}
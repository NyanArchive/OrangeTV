package tv.orange.features.settings.bridge

import dagger.android.AndroidInjector
import tv.orange.features.settings.bridge.fragment.OrangeSettingsFragment

interface OrangeSettingsFragmentSubcomponentBinding :
    AndroidInjector<OrangeSettingsFragment> {
    interface Factory : AndroidInjector.Factory<OrangeSettingsFragment> {
        override fun create(systemSettingsFragment: OrangeSettingsFragment): AndroidInjector<OrangeSettingsFragment>
    }
}
package tv.orange.features.settings.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.di.module.SettingsModule
import tv.orange.features.settings.di.scope.SettingsScope

@SettingsScope
@Component(dependencies = [CoreComponent::class], modules = [SettingsModule::class])
interface SettingsComponent {
    val orangeSettings: OrangeSettings
}
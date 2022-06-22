package tv.orange.features.streamuptime.di

import dagger.Component
import tv.orange.features.core.di.CoreFeatureComponent
import tv.orange.features.streamuptime.Hook

@StreamUptimeScope
@Component(dependencies = [CoreFeatureComponent::class], modules = [StreamUptimeModule::class])
interface StreamUptimeComponent {
    val hook: Hook
}
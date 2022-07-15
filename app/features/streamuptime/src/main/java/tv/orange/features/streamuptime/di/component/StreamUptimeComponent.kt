package tv.orange.features.streamuptime.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.streamuptime.StreamUptimeHookProvider
import tv.orange.features.streamuptime.di.module.StreamUptimeModule
import tv.orange.features.streamuptime.di.scope.StreamUptimeScope

@StreamUptimeScope
@Component(dependencies = [CoreComponent::class], modules = [StreamUptimeModule::class])
interface StreamUptimeComponent {
    val hook: StreamUptimeHookProvider
}
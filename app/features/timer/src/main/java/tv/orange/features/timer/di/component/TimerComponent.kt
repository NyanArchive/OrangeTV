package tv.orange.features.timer.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.timer.Hook
import tv.orange.features.timer.di.module.TimerModule
import tv.orange.features.timer.di.scope.TimerScope

@TimerScope
@Component(dependencies = [CoreComponent::class], modules = [TimerModule::class])
interface TimerComponent {
    val hook: Hook
}
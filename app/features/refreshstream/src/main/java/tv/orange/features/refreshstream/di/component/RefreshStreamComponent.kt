package tv.orange.features.refreshstream.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.refreshstream.Hook
import tv.orange.features.refreshstream.di.module.RefreshStreamModule
import tv.orange.features.refreshstream.di.scope.RefreshStreamScope

@RefreshStreamScope
@Component(dependencies = [CoreComponent::class], modules = [RefreshStreamModule::class])
interface RefreshStreamComponent {
    val hook: Hook
}
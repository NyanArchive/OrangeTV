package tv.orange.features.refreshstream.di

import dagger.Component
import tv.orange.features.core.di.CoreFeatureComponent
import tv.orange.features.refreshstream.Hook

@RefreshStreamScope
@Component(dependencies = [CoreFeatureComponent::class], modules = [RefreshStreamModule::class])
interface RefreshStreamComponent {
    val hook: Hook
}
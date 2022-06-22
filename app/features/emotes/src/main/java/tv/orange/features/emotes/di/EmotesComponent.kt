package tv.orange.features.emotes.di

import dagger.Component
import tv.orange.features.core.di.CoreFeatureComponent
import tv.orange.features.emotes.Hook

@EmotesScope
@Component(
    dependencies = [CoreFeatureComponent::class],
    modules = [EmotesModule::class, ApiModule::class]
)
interface EmotesComponent {
    val hook: Hook
}
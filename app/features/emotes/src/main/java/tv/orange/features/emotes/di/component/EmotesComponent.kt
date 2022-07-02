package tv.orange.features.emotes.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.emotes.EmotesInjector
import tv.orange.features.emotes.di.module.EmotesModule
import tv.orange.features.emotes.di.scope.EmotesScope

@EmotesScope
@Component(
    dependencies = [CoreComponent::class, ApiComponent::class],
    modules = [EmotesModule::class]
)
interface EmotesComponent {
    val emotesInjector: EmotesInjector
}
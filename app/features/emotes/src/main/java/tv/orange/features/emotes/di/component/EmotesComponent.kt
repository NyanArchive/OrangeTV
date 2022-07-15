package tv.orange.features.emotes.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.module.EmotesModule
import tv.orange.features.emotes.di.scope.EmotesScope
import tv.orange.models.ProtoComponent

@EmotesScope
@Component(
    dependencies = [CoreComponent::class, ApiComponent::class],
    modules = [EmotesModule::class]
)
interface EmotesComponent : ProtoComponent {
    val emoteProvider: EmoteProvider
}
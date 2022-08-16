package tv.orange.features.pronouns.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.pronouns.component.PronounProvider
import tv.orange.features.pronouns.di.module.PronounModule
import tv.orange.features.pronouns.di.scope.PronounScope

@PronounScope
@Component(
    dependencies = [CoreComponent::class, ApiComponent::class],
    modules = [PronounModule::class]
)
interface PronounComponent {
    val pronounProvider: PronounProvider
}
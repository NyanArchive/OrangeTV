package tv.orange.features.spam.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.spam.Spam
import tv.orange.features.spam.di.module.SpamModule
import tv.orange.features.spam.di.scope.SpamScope

@SpamScope
@Component(dependencies = [CoreComponent::class], modules = [SpamModule::class])
interface SpamComponent {
    val spam: Spam
}
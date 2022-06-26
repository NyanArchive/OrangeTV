package tv.orange.features.badges.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.badges.BadgesInjector
import tv.orange.features.badges.di.module.BadgesApiModule
import tv.orange.features.badges.di.module.BadgesModule
import tv.orange.features.badges.di.scope.BadgesScope

@BadgesScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [BadgesModule::class, BadgesApiModule::class]
)
interface BadgesComponent {
    val badgesInjector: BadgesInjector
}
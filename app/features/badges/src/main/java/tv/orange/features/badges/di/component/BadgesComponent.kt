package tv.orange.features.badges.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.badges.di.module.BadgesModule
import tv.orange.features.badges.di.scope.BadgesScope
import tv.orange.models.ProtoComponent

@BadgesScope
@Component(
    dependencies = [CoreComponent::class, ApiComponent::class],
    modules = [BadgesModule::class]
)
interface BadgesComponent : ProtoComponent {
    val badgeProvider: BadgeProvider
}
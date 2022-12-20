package tv.orange.features.blacklist.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.blacklist.Blacklist
import tv.orange.features.blacklist.di.module.BlacklistModule
import tv.orange.features.blacklist.di.scope.BlacklistScope

@BlacklistScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [BlacklistModule::class]
)
interface BlacklistComponent {
    val blacklist: Blacklist
}
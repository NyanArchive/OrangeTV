package tv.orange.features.usersearch.di

import dagger.Component
import tv.orange.features.core.di.CoreFeatureComponent
import tv.orange.features.usersearch.Hook

@UserSearchScope
@Component(dependencies = [CoreFeatureComponent::class], modules = [UserSearchModule::class])
interface UserSearchComponent {
    val hook: Hook
}
package tv.orange.features.usersearch.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.usersearch.di.module.UserSearchModule
import tv.orange.features.usersearch.di.scope.UserSearchScope

@UserSearchScope
@Component(dependencies = [CoreComponent::class], modules = [UserSearchModule::class])
interface UserSearchComponent {
    val userSearch: UserSearch
}
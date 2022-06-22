package tv.orange.features.usersearch.di

import dagger.Binds
import dagger.Module
import tv.orange.features.usersearch.view.ViewFactory
import tv.orange.features.usersearch.view.ViewFactoryImpl

@Module(includes = [UserSearchModule.BindsModule::class])
class UserSearchModule {

    @Module
    abstract class BindsModule {
        @UserSearchScope
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
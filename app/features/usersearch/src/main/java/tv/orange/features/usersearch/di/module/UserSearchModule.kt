package tv.orange.features.usersearch.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.usersearch.view.ViewFactory
import tv.orange.features.usersearch.view.ViewFactoryImpl

@Module(includes = [UserSearchModule.BindsModule::class])
class UserSearchModule {
    @Module
    abstract class BindsModule {
        @Binds
        abstract fun bind(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
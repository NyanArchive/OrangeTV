package tv.orange.features.spam.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.spam.component.factory.ChatCommandInterceptorFactory
import tv.orange.features.spam.component.factory.ChatCommandInterceptorFactoryImpl

@Module(includes = [SpamModule.BindsModule::class])
class SpamModule {

    @Module
    abstract class BindsModule {
        @Binds
        abstract fun bindFactory(viewFactory: ChatCommandInterceptorFactoryImpl): ChatCommandInterceptorFactory
    }
}
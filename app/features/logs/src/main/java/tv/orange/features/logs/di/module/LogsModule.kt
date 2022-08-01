package tv.orange.features.logs.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.logs.di.scope.LogsScope
import tv.orange.features.logs.view.ViewFactory
import tv.orange.features.logs.view.ViewFactoryImpl

@Module(includes = [LogsModule.BindsModule::class])
class LogsModule {
    @Module
    abstract class BindsModule {
        @LogsScope
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
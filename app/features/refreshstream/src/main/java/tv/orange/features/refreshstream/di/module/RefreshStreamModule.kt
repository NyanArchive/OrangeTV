package tv.orange.features.refreshstream.di.module

import dagger.Module
import tv.orange.features.refreshstream.di.scope.RefreshStreamScope
import tv.orange.features.refreshstream.view.ViewFactory
import tv.orange.features.refreshstream.view.ViewFactoryImpl

@Module(includes = [RefreshStreamModule.BindsModule::class])
class RefreshStreamModule {

    @Module
    abstract class BindsModule {
        @RefreshStreamScope
        @dagger.Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
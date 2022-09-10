package tv.orange.features.timer.di.module

import dagger.Module
import tv.orange.features.timer.view.ViewFactory
import tv.orange.features.timer.view.ViewFactoryImpl

@Module(includes = [TimerModule.BindsModule::class])
class TimerModule {

    @Module
    abstract class BindsModule {
        @dagger.Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
package tv.orange.features.vodspeed.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.vodspeed.view.ViewFactory
import tv.orange.features.vodspeed.view.ViewFactoryImpl

@Module(includes = [VodSpeedModule.BindsModule::class])
class VodSpeedModule {
    @Module
    abstract class BindsModule {
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
package tv.orange.features.vodsync.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.vodsync.di.scope.VodSyncScope
import tv.orange.features.vodsync.view.ViewFactory
import tv.orange.features.vodsync.view.ViewFactoryImpl

@Module(includes = [VodSyncModule.BindsModule::class])
class VodSyncModule {
    @Module
    abstract class BindsModule {
        @VodSyncScope
        @Binds
        abstract fun bind(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
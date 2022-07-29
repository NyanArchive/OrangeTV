package tv.orange.features.chapters.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.chapters.di.scope.ChaptersScope
import tv.orange.features.chapters.view.ViewFactory
import tv.orange.features.chapters.view.ViewFactoryImpl

@Module(includes = [ChaptersModule.BindsModule::class])
class ChaptersModule {
    @Module
    abstract class BindsModule {
        @ChaptersScope
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
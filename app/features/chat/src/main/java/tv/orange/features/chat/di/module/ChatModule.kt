package tv.orange.features.chat.di.module

import dagger.Binds
import dagger.Module
import tv.orange.features.chat.di.scope.ChatScope
import tv.orange.features.chat.view.ViewFactory
import tv.orange.features.chat.view.ViewFactoryImpl

@Module(includes = [ChatModule.BindsModule::class])
class ChatModule {

    @Module
    abstract class BindsModule {
        @ChatScope
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
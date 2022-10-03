package tv.orange.features.chat.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import tv.orange.features.chat.db.FavEmotesDatabase
import tv.orange.features.chat.view.ViewFactory
import tv.orange.features.chat.view.ViewFactoryImpl

@Module(includes = [ChatModule.BindsModule::class])
class ChatModule {
    @Provides
    fun provideFavEmotesDatabase(): FavEmotesDatabase {
        return FavEmotesDatabase.INSTANCE
    }

    @Module
    abstract class BindsModule {
        @Binds
        abstract fun bindViewFactory(viewFactory: ViewFactoryImpl): ViewFactory
    }
}
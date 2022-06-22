package tv.orange.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import tv.orange.core.Logger
import tv.twitch.android.app.core.ApplicationContext

@Module(includes = [NetworkModule::class])
class CoreModule {
    @AppScope
    @Provides
    fun provideContext(): Context {
        val context = ApplicationContext.getInstance().getContext()
        Logger.debug("provide: $context")
        return context
    }
}
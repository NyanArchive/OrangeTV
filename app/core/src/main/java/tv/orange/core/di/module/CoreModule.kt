package tv.orange.core.di.module

import dagger.Module
import dagger.Provides
import tv.orange.core.di.scope.AppScope
import tv.twitch.android.core.user.TwitchAccountManager

@Module
class CoreModule {
    @AppScope
    @Provides
    fun provideTwitchAccountManager(): TwitchAccountManager {
        return TwitchAccountManager()
    }
}
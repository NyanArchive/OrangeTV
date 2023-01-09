package tv.orange.core.di.module

import dagger.Module
import dagger.Provides
import tv.orange.core.PreferencesManagerCore
import tv.orange.core.di.scope.AppScope
import tv.twitch.android.app.core.ThemeManager
import tv.twitch.android.core.user.TwitchAccountManager

@Module
class CoreModule {
    @AppScope
    @Provides
    fun provideTwitchAccountManager(): TwitchAccountManager {
        return TwitchAccountManager()
    }

    @AppScope
    @Provides
    fun provideThemeManager(): ThemeManager.Companion {
        return ThemeManager.Companion
    }

    @AppScope
    @Provides
    fun providePreferenceManagerCore(): PreferencesManagerCore {
        return PreferencesManagerCore
    }
}
package tv.orange.bridge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.features.chapters.di.module.ChaptersModule
import tv.orange.features.chat.di.module.ChatModule
import tv.orange.features.logs.di.module.LogsModule
import tv.orange.features.refreshstream.di.module.RefreshStreamModule
import tv.orange.features.spam.di.module.SpamModule
import tv.orange.features.timer.di.module.TimerModule
import tv.orange.features.usersearch.di.module.UserSearchModule
import tv.orange.features.vodsync.di.module.VodSyncModule
import tv.twitch.android.app.core.ThemeManager

@Module(
    includes = [ChaptersModule::class, ChatModule::class, LogsModule::class,
        RefreshStreamModule::class, SpamModule::class, TimerModule::class,
        UserSearchModule::class, VodSyncModule::class]
)
class BridgeFeatureModule {
    @BridgeScope
    @Provides
    fun providePreferenceManager(context: Context, tm: ThemeManager.Companion): PreferenceManager {
        return PreferenceManager(context, tm)
    }

    @BridgeScope
    @Provides
    fun provideResourceManager(context: Context): ResourceManager {
        return ResourceManager(context)
    }
}
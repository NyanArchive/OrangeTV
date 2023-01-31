package tv.orange.bridge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.ResourcesManagerCore
import tv.orange.features.chapters.di.module.ChaptersModule
import tv.orange.features.chat.di.module.ChatModule
import tv.orange.features.logs.di.module.LogsModule
import tv.orange.features.refreshstream.di.module.RefreshStreamModule
import tv.orange.features.spam.di.module.SpamModule
import tv.orange.features.timer.di.module.TimerModule
import tv.orange.features.usersearch.di.module.UserSearchModule
import tv.orange.features.vodsync.di.module.VodSyncModule

@Module(
    includes = [ChaptersModule::class, ChatModule::class, LogsModule::class,
        RefreshStreamModule::class, SpamModule::class, TimerModule::class,
        UserSearchModule::class, VodSyncModule::class]
)
class BridgeFeatureModule {
    @BridgeScope
    @Provides
    fun provideResourcesManager(context: Context): ResourcesManagerCore {
        return ResourcesManagerCore(context)
    }
}
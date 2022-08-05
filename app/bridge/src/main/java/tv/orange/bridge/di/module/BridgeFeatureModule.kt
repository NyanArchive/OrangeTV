package tv.orange.bridge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.chapters.VodChapters
import tv.orange.features.chapters.di.component.DaggerChaptersComponent
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.chat.di.component.DaggerChatComponent
import tv.orange.features.chathistory.ChatHistory
import tv.orange.features.chathistory.di.component.DaggerChatHistoryComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.features.logs.ChatLogs
import tv.orange.features.logs.di.component.DaggerLogsComponent
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.refreshstream.di.component.DaggerRefreshStreamComponent
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.di.component.DaggerSettingsComponent
import tv.orange.features.stv.StvAvatars
import tv.orange.features.stv.di.component.DaggerStvComponent
import tv.orange.features.timer.SleepTimer
import tv.orange.features.timer.di.component.DaggerTimerComponent
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.usersearch.di.component.DaggerUserSearchComponent
import tv.orange.models.abc.Injector
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory

@Module
class BridgeFeatureModule {
    @Provides
    fun provideRefreshStreamHook(coreComponent: CoreComponent): RefreshStream {
        return DaggerRefreshStreamComponent.builder()
            .coreComponent(coreComponent)
            .build().refreshStream
    }

    @Provides
    fun provideUserSearch(coreComponent: CoreComponent): UserSearch {
        return DaggerUserSearchComponent.builder().coreComponent(coreComponent).build().userSearch
    }

    @Provides
    fun provideSleepTimer(coreComponent: CoreComponent): SleepTimer {
        return DaggerTimerComponent.builder().coreComponent(coreComponent).build().sleepTimer
    }

    @Provides
    fun provideStvAvatars(coreComponent: CoreComponent, apiComponent: ApiComponent): StvAvatars {
        return DaggerStvComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent).build().stvAvatars
    }

    @Provides
    fun provideOrangeSettings(coreComponent: CoreComponent): OrangeSettings {
        return DaggerSettingsComponent.factory().create(
            coreComponent = coreComponent
        ).orangeSettings
    }

    @Provides
    fun provideChatLogs(coreComponent: CoreComponent, injector: Injector): ChatLogs {
        return DaggerLogsComponent.factory().create(
            coreComponent = coreComponent,
            service = injector.getComponentProvider(GraphQlService::class).get(),
            factory = injector.getComponentProvider(ChatMessageFactory.Factory::class).get()
        ).chatLogs
    }

    @Provides
    fun provideChatHistory(coreComponent: CoreComponent, injector: Injector): ChatHistory {
        return DaggerChatHistoryComponent.factory().create(
            coreComponent = coreComponent,
            service = injector.getComponentProvider(GraphQlService::class).get()
        ).chatHistory
    }

    @Provides
    fun provideChaptersHook(coreComponent: CoreComponent, injector: Injector): VodChapters {
        return DaggerChaptersComponent.factory().create(
            coreComponent = coreComponent,
            service = injector.getComponentProvider(GraphQlService::class).get()
        ).vodChapters
    }

    @Provides
    fun provideChatHookProvider(
        badgesComponent: BadgesComponent,
        emotesComponent: EmotesComponent
    ): ChatHookProvider {
        return DaggerChatComponent.builder()
            .badgesComponent(badgesComponent)
            .emotesComponent(emotesComponent)
            .build().hook
    }

    @BridgeScope
    @Provides
    fun providePreferenceManager(context: Context): PreferenceManager {
        return PreferenceManager(context)
    }

    @BridgeScope
    @Provides
    fun provideResourceManager(context: Context): ResourceManager {
        return ResourceManager(context)
    }
}
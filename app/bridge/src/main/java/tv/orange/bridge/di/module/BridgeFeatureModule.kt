package tv.orange.bridge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.Logger
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
import tv.orange.features.pronouns.di.component.PronounComponent
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.refreshstream.di.component.DaggerRefreshStreamComponent
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.settings.di.component.DaggerSettingsComponent
import tv.orange.features.spam.Spam
import tv.orange.features.spam.di.component.DaggerSpamComponent
import tv.orange.features.stv.StvAvatars
import tv.orange.features.stv.di.component.DaggerStvComponent
import tv.orange.features.timer.SleepTimer
import tv.orange.features.timer.di.component.DaggerTimerComponent
import tv.orange.features.tracking.Tracking
import tv.orange.features.tracking.di.component.DaggerTrackingComponent
import tv.orange.features.ui.UI
import tv.orange.features.ui.di.component.DaggerUIComponent
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.usersearch.di.component.DaggerUserSearchComponent
import tv.orange.features.vodsync.VodSync
import tv.orange.features.vodsync.di.component.DaggerVodSyncComponent
import tv.orange.models.abc.TwitchComponentProvider
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
        return DaggerUserSearchComponent.builder()
            .coreComponent(coreComponent).build().userSearch
    }

    @Provides
    fun provideSleepTimer(coreComponent: CoreComponent): SleepTimer {
        return DaggerTimerComponent.builder()
            .coreComponent(coreComponent).build().sleepTimer
    }

    @Provides
    fun provideStvAvatars(coreComponent: CoreComponent, apiComponent: ApiComponent): StvAvatars {
        return DaggerStvComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent).build().stvAvatars
    }

    @Provides
    fun provideOrangeSettings(coreComponent: CoreComponent): OrangeSettings {
        return DaggerSettingsComponent.builder()
            .coreComponent(coreComponent).build().orangeSettings
    }

    @Provides
    fun provideChatLogs(
        coreComponent: CoreComponent,
        tcp: TwitchComponentProvider
    ): ChatLogs {
        return DaggerLogsComponent.factory().create(
            coreComponent = coreComponent,
            service = tcp.getProvider(GraphQlService::class).get(),
            factory = tcp.getProvider(ChatMessageFactory.Factory::class).get()
        ).chatLogs
    }

    @Provides
    fun provideChatHistory(
        coreComponent: CoreComponent,
        tcp: TwitchComponentProvider
    ): ChatHistory {
        return DaggerChatHistoryComponent.factory().create(
            coreComponent = coreComponent,
            service = tcp.getProvider(GraphQlService::class).get()
        ).chatHistory
    }

    @Provides
    fun provideChaptersHook(
        coreComponent: CoreComponent,
        resourceManager: ResourceManager,
        tcp: TwitchComponentProvider
    ): VodChapters {
        return DaggerChaptersComponent.factory().create(
            coreComponent = coreComponent,
            resourceManager = resourceManager,
            service = tcp.getProvider(GraphQlService::class).get()
        ).vodChapters
    }

    @Provides
    fun provideVodSync(
        coreComponent: CoreComponent
    ): VodSync {
        return DaggerVodSyncComponent.builder()
            .coreComponent(coreComponent).build().vodSync
    }

    @Provides
    fun provideChatHookProvider(
        coreComponent: CoreComponent,
        badgesComponent: BadgesComponent,
        emotesComponent: EmotesComponent,
        pronounComponent: PronounComponent
    ): ChatHookProvider {
        return DaggerChatComponent.builder()
            .coreComponent(coreComponent)
            .badgesComponent(badgesComponent)
            .emotesComponent(emotesComponent)
            .pronounComponent(pronounComponent)
            .build().hook
    }

    @Provides
    fun provideUIProvider(coreComponent: CoreComponent): UI {
        return DaggerUIComponent.builder()
            .coreComponent(coreComponent).build().ui
    }

    @Provides
    fun provideSpamProvider(coreComponent: CoreComponent): Spam {
        return DaggerSpamComponent.builder()
            .coreComponent(coreComponent).build().spam
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

    @BridgeScope
    @Provides
    fun provideTracking(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): Tracking {
        return DaggerTrackingComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent)
            .build().tracking
    }
}
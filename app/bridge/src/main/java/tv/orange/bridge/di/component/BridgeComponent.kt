package tv.orange.bridge.di.component

import dagger.Component
import tv.orange.bridge.di.module.BridgeModule
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.Core
import tv.orange.core.CoreHook
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.chapters.VodChapters
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.chathistory.ChatHistory
import tv.orange.features.logs.ChatLogs
import tv.orange.features.updater.Updater
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.spam.Spam
import tv.orange.features.stv.StvAvatars
import tv.orange.features.timer.SleepTimer
import tv.orange.features.tracking.Tracking
import tv.orange.features.ui.UI
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.vodhunter.Vodhunter
import tv.orange.features.vodsync.VodSync
import javax.inject.Provider

@BridgeScope
@Component(dependencies = [CoreComponent::class], modules = [BridgeModule::class])
interface BridgeComponent {
    val refreshStreamProvider: Provider<RefreshStream>
    val chatHookProvider: Provider<ChatHookProvider>
    val vodChaptersProvider: Provider<VodChapters>
    val chatHistoryProvider: Provider<ChatHistory>
    val chatLogsProvider: Provider<ChatLogs>
    val orangeSettingsProvider: Provider<OrangeSettings>
    val stvAvatarsProvider: Provider<StvAvatars>
    val sleepTimerProvider: Provider<SleepTimer>
    val userSearchProvider: Provider<UserSearch>
    val resourceManagerProvider: Provider<ResourceManager>
    val vodSyncProvider: Provider<VodSync>
    val coreHookProvider: Provider<CoreHook>
    val uiProvider: Provider<UI>
    val spamProvider: Provider<Spam>
    val trackingProvider: Provider<Tracking>
    val updaterProvider: Provider<Updater>
    val vodhunterProvider: Provider<Vodhunter>

    val preferenceManager: Provider<PreferenceManager>
    val core: Provider<Core>
}
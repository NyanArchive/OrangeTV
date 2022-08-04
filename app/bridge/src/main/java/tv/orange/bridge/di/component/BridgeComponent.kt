package tv.orange.bridge.di.component

import dagger.Component
import tv.orange.bridge.di.module.BridgeModule
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.chapters.VodChapters
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.logs.ChatLogs
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.stv.StvAvatars
import tv.orange.features.timer.SleepTimer
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.chathistory.ChatHistory
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

    val preferenceManager: Provider<PreferenceManager>
    val core: Provider<Core>
}
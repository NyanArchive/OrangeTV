package tv.orange.bridge.di

import android.content.Context
import tv.orange.bridge.di.component.BridgeComponent
import tv.orange.bridge.di.component.DaggerBridgeComponent
import tv.orange.core.Core
import tv.orange.core.CoreHook
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.di.component.DaggerCoreComponent
import tv.orange.features.chapters.VodChapters
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.chathistory.ChatHistory
import tv.orange.features.logs.ChatLogs
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.spam.Spam
import tv.orange.features.stv.StvAvatars
import tv.orange.features.timer.SleepTimer
import tv.orange.features.ui.UI
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.vodsync.VodSync
import tv.orange.models.abc.Feature
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider
import kotlin.collections.set

class Bridge private constructor() : tv.orange.models.abc.Bridge {
    private lateinit var component: BridgeComponent

    private val lock = Any()

    private val factory = ConcurrentHashMap<Class<*>, () -> Provider<out Feature>>()

    private val map = ConcurrentHashMap<Class<*>, Provider<out Feature>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Feature> getFeature(clazz: Class<T>): T {
        synchronized(lock) {
            map[clazz]?.let {
                return it.get() as T
            }
            factory[clazz]?.let { func ->
                func().run {
                    val feature = get()
                    feature.onCreateFeature()
                    map[clazz] = Provider { feature }
                    return feature as T
                }
            }
        }

        throw IllegalStateException("Cannot create $clazz feature: fabric not found")
    }

    override fun <T : Feature> destroyFeature(clazz: Class<T>) {
        synchronized(lock) {
            val feature = map[clazz]?.get()
            map.remove(clazz)
            feature?.onDestroyFeature()
        }
    }

    private fun buildFactoryMap() {
        factory[PreferenceManager::class.java] = { component.preferenceManager }
        factory[Core::class.java] = { component.core }
        factory[RefreshStream::class.java] = { component.refreshStreamProvider }
        factory[ChatHookProvider::class.java] = { component.chatHookProvider }
        factory[VodChapters::class.java] = { component.vodChaptersProvider }
        factory[ChatHistory::class.java] = { component.chatHistoryProvider }
        factory[ChatLogs::class.java] = { component.chatLogsProvider }
        factory[OrangeSettings::class.java] = { component.orangeSettingsProvider }
        factory[StvAvatars::class.java] = { component.stvAvatarsProvider }
        factory[SleepTimer::class.java] = { component.sleepTimerProvider }
        factory[UserSearch::class.java] = { component.userSearchProvider }
        factory[ResourceManager::class.java] = { component.resourceManagerProvider }
        factory[VodSync::class.java] = { component.vodSyncProvider }
        factory[CoreHook::class.java] = { component.coreHookProvider }
        factory[UI::class.java] = { component.uiProvider }
        factory[Spam::class.java] = { component.spamProvider }
    }

    fun initialize(context: Context) {
        component = DaggerBridgeComponent.builder().coreComponent(
            DaggerCoreComponent.factory().create(context)
        ).build()
        buildFactoryMap()
    }

    fun initializeFeatures() {
        getFeature(PreferenceManager::class.java).initialize()
        getFeature(Core::class.java)
        getFeature(ChatHookProvider::class.java)
        getFeature(StvAvatars::class.java)
    }

    companion object {
        @JvmStatic
        fun create(): Bridge {
            return Bridge()
        }
    }
}
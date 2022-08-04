package tv.orange.bridge.di

import tv.orange.bridge.di.component.BridgeComponent
import tv.orange.bridge.di.component.DaggerBridgeComponent
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.features.chapters.VodChapters
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.logs.ChatLogs
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.stv.StvAvatars
import tv.orange.features.timer.SleepTimer
import tv.orange.features.usersearch.UserSearch
import tv.orange.models.Feature
import tv.orange.features.chathistory.ChatHistory
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider
import kotlin.collections.set

class Bridge private constructor() : tv.orange.models.Bridge {
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
                    initialize()
                    map[clazz] = this
                    return get() as T
                }
            }
        }

        throw IllegalStateException("Cannot create $clazz feature: fabric not found")
    }

    override fun <T : Feature> destroyFeature(clazz: Class<T>) {
        synchronized(lock) {
            map[clazz]?.get()?.onDestroyFeature()
            map.remove(clazz)
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
    }

    fun initialize() {
        component = DaggerBridgeComponent.builder().build()
        buildFactoryMap()
        getFeature(PreferenceManager::class.java).initialize() // init first
    }

    companion object {
        @JvmStatic
        fun create(): Bridge {
            return Bridge()
        }
    }
}
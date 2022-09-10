package tv.orange.bridge.di

import android.content.Context
import tv.orange.bridge.di.component.BridgeComponent
import tv.orange.bridge.di.component.DaggerBridgeComponent
import tv.orange.core.*
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
import tv.orange.features.tracking.Tracking
import tv.orange.features.ui.UI
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.vodsync.VodSync
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import javax.inject.Provider
import kotlin.collections.set

class BridgeImpl private constructor() : Bridge {
    private lateinit var component: BridgeComponent

    private val lock = Any()

    private val featureFactoryMap = LinkedHashMap<Class<*>, () -> Provider<out Feature>>()
    private val clazzProviderMap = LinkedHashMap<Class<*>, Provider<out Feature>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Feature> getFeature(clazz: Class<T>): T {
        synchronized(lock) {
            clazzProviderMap[clazz]?.let { provider ->
                return provider.get() as T
            }
            createFeature(clazz).let { feature ->
                return feature
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Feature> createFeature(clazz: Class<T>): T {
        synchronized(lock) {
            featureFactoryMap[clazz]?.let { providerFunc ->
                providerFunc().let { provider ->
                    val feature = provider.get()
                    feature.onCreateFeature()
                    clazzProviderMap[clazz] = Provider { feature }
                    return feature as T
                }
            }
        }

        throw IllegalStateException("[$clazz] Cannot create feature: factory not found")
    }

    override fun <T : Feature> destroyFeature(clazz: Class<T>) {
        synchronized(lock) {
            val feature = clazzProviderMap[clazz]?.get()
            clazzProviderMap.remove(clazz)
            feature?.onDestroyFeature()
        }
    }

    private fun buildFactoryMap() {
        featureFactoryMap[PreferenceManager::class.java] = { component.preferenceManager }
        featureFactoryMap[Core::class.java] = { component.core }
        featureFactoryMap[ResourceManager::class.java] = { component.resourceManagerProvider }
        featureFactoryMap[StvAvatars::class.java] = { component.stvAvatarsProvider }
        featureFactoryMap[ChatHookProvider::class.java] = { component.chatHookProvider }
        featureFactoryMap[RefreshStream::class.java] = { component.refreshStreamProvider }
        featureFactoryMap[VodChapters::class.java] = { component.vodChaptersProvider }
        featureFactoryMap[ChatHistory::class.java] = { component.chatHistoryProvider }
        featureFactoryMap[ChatLogs::class.java] = { component.chatLogsProvider }
        featureFactoryMap[OrangeSettings::class.java] = { component.orangeSettingsProvider }
        featureFactoryMap[SleepTimer::class.java] = { component.sleepTimerProvider }
        featureFactoryMap[UserSearch::class.java] = { component.userSearchProvider }
        featureFactoryMap[VodSync::class.java] = { component.vodSyncProvider }
        featureFactoryMap[CoreHook::class.java] = { component.coreHookProvider }
        featureFactoryMap[UI::class.java] = { component.uiProvider }
        featureFactoryMap[Spam::class.java] = { component.spamProvider }
        featureFactoryMap[Tracking::class.java] = { component.trackingProvider }
    }

    fun initialize(context: Context) {
        component = DaggerBridgeComponent.builder()
            .coreComponent(DaggerCoreComponent.factory().create(context)).build()
        buildFactoryMap()
    }

    @Suppress("UNCHECKED_CAST")
    fun initializeFeatures() {
        for (clazz in featureFactoryMap.keys) {
            if (hasAutoInitAnnotation(clazz)) {
                createFeature(clazz as Class<Feature>)
            }
        }
    }

    companion object {
        @JvmStatic
        fun create(): BridgeImpl {
            return BridgeImpl()
        }

        private fun hasAutoInitAnnotation(clazz: Class<*>): Boolean {
            return clazz.annotations.any { annotation -> annotation is AutoInitialize }
        }
    }
}
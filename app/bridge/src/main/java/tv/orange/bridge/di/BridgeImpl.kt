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
import tv.orange.features.proxy.Proxy
import tv.orange.features.refreshstream.RefreshStream
import tv.orange.features.settings.OrangeSettings
import tv.orange.features.spam.Spam
import tv.orange.features.stv.StvAvatars
import tv.orange.features.swipper.Swipper
import tv.orange.features.timer.SleepTimer
import tv.orange.features.tracking.Tracking
import tv.orange.features.ui.UI
import tv.orange.features.updater.Updater
import tv.orange.features.usersearch.UserSearch
import tv.orange.features.vodhunter.Vodhunter
import tv.orange.features.vodsync.VodSync
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import javax.inject.Provider
import kotlin.collections.set

class BridgeImpl private constructor() : Bridge {
    private lateinit var component: BridgeComponent

    private val lock = Any()

    private val featureFactoryMap = LinkedHashMap<Class<*>, Lazy<Provider<out Feature>>>()
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
            featureFactoryMap[clazz]?.value?.let { provider ->
                val feature = provider.get()
                feature.onCreateFeature()
                clazzProviderMap[clazz] = Provider { feature }
                return feature as T
            }
        }

        throw IllegalStateException("[$clazz] Cannot create feature: factory not found")
    }

    private fun buildFactoryMap() {
        // core
        featureFactoryMap[Core::class.java] = lazy { component.core }
        featureFactoryMap[ResourceManager::class.java] = lazy { component.resourceManagerProvider }
        featureFactoryMap[PreferenceManager::class.java] = lazy { component.preferenceManager }
        // features
        featureFactoryMap[StvAvatars::class.java] = lazy { component.stvAvatarsProvider }
        featureFactoryMap[ChatHookProvider::class.java] = lazy { component.chatHookProvider }
        featureFactoryMap[RefreshStream::class.java] = lazy { component.refreshStreamProvider }
        featureFactoryMap[VodChapters::class.java] = lazy { component.vodChaptersProvider }
        featureFactoryMap[ChatHistory::class.java] = lazy { component.chatHistoryProvider }
        featureFactoryMap[ChatLogs::class.java] = lazy { component.chatLogsProvider }
        featureFactoryMap[OrangeSettings::class.java] = lazy { component.orangeSettingsProvider }
        featureFactoryMap[SleepTimer::class.java] = lazy { component.sleepTimerProvider }
        featureFactoryMap[UserSearch::class.java] = lazy { component.userSearchProvider }
        featureFactoryMap[VodSync::class.java] = lazy { component.vodSyncProvider }
        featureFactoryMap[CoreHook::class.java] = lazy { component.coreHookProvider }
        featureFactoryMap[UI::class.java] = lazy { component.uiProvider }
        featureFactoryMap[Spam::class.java] = lazy { component.spamProvider }
        featureFactoryMap[Tracking::class.java] = lazy { component.trackingProvider }
        featureFactoryMap[Updater::class.java] = lazy { component.updaterProvider }
        featureFactoryMap[Vodhunter::class.java] = lazy { component.vodhunterProvider }
        featureFactoryMap[Swipper::class.java] = lazy { component.swipperProvider }
        featureFactoryMap[Proxy::class.java] = lazy { component.proxyProvider }
    }

    fun initialize(context: Context) {
        component = DaggerBridgeComponent.builder().coreComponent(
            DaggerCoreComponent.factory()
                .create(context)
        ).build()
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
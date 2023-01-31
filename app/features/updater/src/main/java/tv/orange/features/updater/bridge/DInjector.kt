package tv.orange.features.updater.bridge

import androidx.work.ListenableWorker
import tv.orange.features.updater.work.*
import tv.twitch.android.core.work.AssistedWorkerFactory
import javax.inject.Provider

object DInjector {
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun injectDownloadUpdateWorker(map: Map<Class<out ListenableWorker>, Provider<AssistedWorkerFactory<out ListenableWorker>>>): MutableMap<Class<out ListenableWorker>, Provider<AssistedWorkerFactory<out ListenableWorker>>> {
        val imap = map.toMutableMap()
        imap[DownloadUpdateRxWorker::class.java] = DownloadUpdateRxWorker_Factory_Impl.create(
            DownloadUpdateRxWorker_Factory.create()
        ) as Provider<AssistedWorkerFactory<out ListenableWorker>>
        imap[InstallUpdateWorker::class.java] = InstallUpdateWorker_Factory_Impl.create(
            InstallUpdateWorker_Factory.create()
        ) as Provider<AssistedWorkerFactory<out ListenableWorker>>

        return imap
    }
}
package tv.orange.features.updater

import android.content.Context
import androidx.work.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Core
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.UpdateChannel
import tv.orange.core.util.FileUtil.deleteDir
import tv.orange.core.util.FileUtil.dirSize
import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.component.data.repository.UpdaterRepository
import tv.orange.features.updater.data.view.UpdaterActivity
import tv.orange.features.updater.work.DownloadUpdateRxWorker
import tv.orange.features.updater.work.InstallUpdateWorker
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.rxutil.ISubscriptionHelper
import tv.twitch.android.feature.update.UpdatePromptPresenter
import tv.twitch.android.util.Optional
import java.io.File
import javax.inject.Inject

class Updater @Inject constructor(
    val resourceManager: ResourcesManagerCore,
    val updaterRepository: UpdaterRepository,
    val context: Context
) : Feature {
    private val disposables = CompositeDisposable()

    private var updateData: Optional<UpdateData> = Optional.empty()

    companion object {
        const val TEMP_OTA_DIR = "tmp_ota"
        const val INSTALL_OTA_DIR = "install_ota"
        const val DOWNLOAD_TASK_NAME = "DownloadUpdate"

        @JvmStatic
        fun get() = Core.getFeature(Updater::class.java)

        fun getTempDir(context: Context): File {
            val tmp = File(context.cacheDir, TEMP_OTA_DIR)
            if (tmp.exists()) {
                return tmp
            }
            tmp.mkdir()

            return tmp
        }

        fun getOtaDir(context: Context): File {
            val ota = File(context.cacheDir, INSTALL_OTA_DIR)
            if (ota.exists()) {
                return ota
            }
            ota.mkdir()

            return ota
        }

        @JvmStatic
        val orangeAppUpdateAvailable: Int = ResourcesManagerCore.get().getStringId(
            "orange_app_update_available"
        )

        @JvmStatic
        val orangeAppUpdateAvailableCta: Int = ResourcesManagerCore.get().getStringId(
            "orange_app_update_available_cta"
        )

        private fun createUrlData(url: String, build: Int): Data = Data.Builder().apply {
            putString(DownloadUpdateRxWorker.URL_KEY, url)
            putInt(DownloadUpdateRxWorker.BUILD_KEY, build)
        }.build()
    }

    fun onClearCacheClicked(context: Context) {
        clearTempCache(context = context)
        clearOtaDir(context = context)

        Core.showToast(resourceManager.getString("orange_updater_done"))
    }

    fun onCheckUpdateClicked(context: Context) {
        clearTempCache(context = context)
        when (Flag.UPDATER.asVariant<UpdateChannel>()) {
            UpdateChannel.Disabled -> {
                Core.showToast(resourceManager.getString("orange_updater_channel_disabled"))
                return
            }
            else -> {
                maybeStartActivity(context = context)
            }
        }
    }

    fun onBannerInstallUpdateClicked(context: Context) {
        updateData.ifPresent { data ->
            UpdaterActivity.startActivity(context = context, data = data)
        }
    }

    private fun maybeStartActivity(context: Context) {
        disposables.add(
            updaterRepository.observeUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ optionalData ->
                    checkUpdateData(context, optionalData)
                }, Throwable::printStackTrace)
        )
    }

    private fun checkUpdateData(
        context: Context,
        optional: Optional<UpdateData>
    ) {
        if (!optional.isPresent) {
            Core.showToast(resourceManager.getString("orange_updater_not_found"))
            return
        }

        optional.ifPresent { data ->
            if (!shouldShowPrompt(optional)) {
                Core.showToast(resourceManager.getString("orange_updater_latest_version"))
                return@ifPresent
            }

            UpdaterActivity.startActivity(context = context, data = data)
        }
    }

    fun clearTempCache(context: Context) {
        getTempDir(context = context).deleteDir()
    }

    fun clearOtaDir(context: Context) {
        getOtaDir(context = context).deleteDir()
    }

    fun createTempFile(): File {
        return File(getTempDir(context), "${System.currentTimeMillis()}.tmp")
    }

    fun getOtaFile(build: Int): File {
        return File(getOtaDir(context), "$build.apk")
    }

    fun injectToUpdatePromptPresenter(
        updatePromptPresenter: UpdatePromptPresenter,
        listenerBehaviorSubject: BehaviorSubject<Optional<UpdatePromptPresenter.UpdatePromptPresenterListener>>
    ) {
        ISubscriptionHelper.DefaultImpls.`autoDispose$default`(
            updatePromptPresenter,
            updaterRepository.observeUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapObservable { updateData ->
                    updatePromptPresenter.onActiveObserver().toObservable().switchMap { status ->
                        listenerBehaviorSubject.map { listenerOptional ->
                            Triple(updateData, status, listenerOptional)
                        }
                    }
                }.subscribe({
                    updateData = it.first
                    if (it.second) {
                        it.third.ifPresent { listener ->
                            if (shouldShowPrompt(it.first)) {
                                listener.updateDownloadedAndReadyToInstall()
                            }
                        }
                    }
                }, {
                    it.printStackTrace()
                }),
            null,
            1,
            null
        )
        updatePromptPresenter.onActiveObserver()
    }

    private fun shouldShowPrompt(first: Optional<UpdateData>): Boolean {
        if (!first.isPresent) {
            return false
        }

        return !(!Flag.FORCE_OTA.asBoolean() && (first.get().build <= BuildConfigUtil.buildConfig.number))
    }

    fun calcCacheSize(context: Context): Long {
        return getOtaDir(context).dirSize() + getTempDir(context).dirSize()
    }

    fun scheduleWork(url: String, build: Int) {
        val request = OneTimeWorkRequest.Builder(DownloadUpdateRxWorker::class.java)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .setInputData(createUrlData(url, build))
            .build()

        WorkManager.getInstance(context).beginUniqueWork(
            DOWNLOAD_TASK_NAME,
            ExistingWorkPolicy.KEEP,
            request
        ).then(OneTimeWorkRequest.Builder(InstallUpdateWorker::class.java).build()).enqueue()
    }

    override fun onCreateFeature() {
        WorkManager.getInstance(context).cancelUniqueWork(DOWNLOAD_TASK_NAME)
    }
}
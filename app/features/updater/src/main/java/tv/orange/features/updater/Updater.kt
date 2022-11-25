package tv.orange.features.updater

import android.content.Context
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.UpdateChannel
import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.component.data.repository.UpdaterRepository
import tv.orange.features.updater.data.view.UpdaterActivity
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.rxutil.ISubscriptionHelper
import tv.twitch.android.feature.update.UpdatePromptPresenter
import tv.twitch.android.util.Optional
import java.io.File
import javax.inject.Inject

class Updater @Inject constructor(
    val resourceManager: ResourceManager,
    val updaterRepository: UpdaterRepository
) : Feature {
    private val disposables = CompositeDisposable()

    private var updateData: Optional<UpdateData> = Optional.empty()

    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Updater::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(Updater::class.java)
        }

        fun getTempDir(context: Context): File {
            val tmp = File(context.cacheDir, UpdaterActivity.TEMP_OTA_DIR)
            if (tmp.exists()) {
                return tmp
            }
            tmp.mkdir()

            return tmp
        }

        fun getOtaDir(context: Context): File {
            val ota = File(context.cacheDir, UpdaterActivity.INSTALL_OTA_DIR)
            if (ota.exists()) {
                return ota
            }
            ota.mkdir()

            return ota
        }

        fun File.deleteDir() {
            deleteRecursive(this)
        }

        private fun deleteRecursive(fileOrDirectory: File) {
            for (child in fileOrDirectory.listFiles() ?: emptyArray()) {
                deleteRecursive(child)
            }

            fileOrDirectory.delete()
        }

        @JvmStatic
        val orangeAppUpdateAvailable: Int = ResourceManager.get().getStringId(
            "orange_app_update_available"
        )

        @JvmStatic
        val orangeAppUpdateAvailableCta: Int = ResourceManager.get().getStringId(
            "orange_app_update_available_cta"
        )
    }

    fun checkUpdates(context: Context) {
        clearCache(context = context)
        LoggerImpl.debug("called")
        when (Flag.UPDATER.asVariant<UpdateChannel>()) {
            UpdateChannel.Disabled -> {
                LoggerImpl.debug("DISABLED")
                Core.toast(resourceManager.getString("orange_updater_channel_disabled"))
                return
            }
            else -> {
                fetchDataAndCheck(context = context)
            }
        }
    }

    private fun fetchDataAndCheck(context: Context) {
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
            Core.toast(resourceManager.getString("orange_updater_not_found"))
            return
        }

        optional.ifPresent { data ->
            if (!needShowPrompt(optional)) {
                Core.toast(resourceManager.getString("orange_updater_latest_version"))
                return@ifPresent
            }

            UpdaterActivity.startActivity(context = context, data = data)
        }
    }

    fun clearCache(context: Context) {
        getTempDir(context = context).deleteDir()
        getOtaDir(context = context).deleteDir()
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}

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
                            if (needShowPrompt(it.first)) {
                                listener.updateDownloadedAndReadyToInstall()
                            }
                        }
                    }
                }, {
                    it.printStackTrace()
                    LoggerImpl.debug("ex: $it")
                }),
            null,
            1,
            null
        )
        updatePromptPresenter.onActiveObserver()
    }

    private fun needShowPrompt(first: Optional<UpdateData>): Boolean {
        if (!first.isPresent) {
            return false
        }

        return !(!Flag.DEV_MODE.asBoolean() && (first.get().build <= BuildConfigUtil.buildConfig.number))
    }

    fun installUpdate(context: Context) {
        updateData.ifPresent { data ->
            UpdaterActivity.startActivity(context = context, data = data)
        }
    }
}
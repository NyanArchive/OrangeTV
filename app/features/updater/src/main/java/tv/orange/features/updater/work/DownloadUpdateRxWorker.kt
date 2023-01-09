package tv.orange.features.updater.work

import android.content.Context
import android.text.format.Formatter
import androidx.core.app.NotificationCompat
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.util.FileUtil.copyTo
import tv.orange.core.util.FileUtil.deleteDir
import tv.orange.core.util.NetUtil
import tv.orange.core.util.NotificationsUtil
import tv.orange.features.updater.Updater
import tv.twitch.android.core.work.AssistedWorkerFactory
import java.io.File
import java.net.URL
import java.util.*

class DownloadUpdateRxWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) :
    RxWorker(context, workerParams), NetUtil.DownloadCallback {
    private var lastNotify: Long = 0

    @AssistedFactory
    interface Factory : AssistedWorkerFactory<DownloadUpdateRxWorker>

    private val builder: NotificationCompat.Builder by lazy {
        val cancel = ResourcesManagerCore.get().getString(android.R.string.cancel)
        val intent = WorkManager.getInstance(context).createCancelPendingIntent(id)

        NotificationCompat.Builder(context, NotificationsUtil.CHANNEL_ID).apply {
            setSmallIcon(android.R.drawable.ic_delete)
            setAutoCancel(false)
            setOngoing(true)
            setOnlyAlertOnce(true)
            setContentTitle(ResourcesManagerCore.get().getString("orangetv_downloading"))
            addAction(android.R.drawable.ic_delete, cancel, intent)
            setProgress(100, 0, true)
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(NotificationsUtil.UPDATER_ID, builder.apply {
            setProgress(100, 0, true)
        }.build())
    }

    private fun createForegroundInfo(state: Int, downloaded: Long, total: Long): ForegroundInfo {
        return ForegroundInfo(NotificationsUtil.UPDATER_ID, builder.apply {
            setProgress(100, state, false)
            setContentText(
                String.format(
                    Locale.ENGLISH, "%1\$s/%2\$s",
                    Formatter.formatFileSize(applicationContext, downloaded),
                    Formatter.formatFileSize(applicationContext, total)
                )
            )
        }.build())
    }

    private fun updateNotifyInfo(state: Int = -1, downloaded: Long = -1L, total: Long = -1L) {
        val info = if (total > 0) {
            createForegroundInfo(state, downloaded, total)
        } else {
            createForegroundInfo()
        }

        setForegroundAsync(info)
        setProgressAsync(Data.Builder().apply {
            putInt("state", state)
            putInt("downloaded", downloaded.toInt())
        }.build())
    }

    private fun updateForegroundInfo(state: Int, downloaded: Long, total: Long) {
        val currentTime = System.currentTimeMillis()
        if (lastNotify <= 0 || currentTime - lastNotify > MIN_NOTIFY_TIMEOUT) {
            updateNotifyInfo(state = state, downloaded = downloaded, total = total)
            lastNotify = currentTime
        }
    }

    override fun onProgressUpdate(progress: Int, downloadedBytes: Int, totalBytes: Int) {
        updateForegroundInfo(
            state = progress,
            downloaded = downloadedBytes.toLong(),
            total = totalBytes.toLong()
        )
    }

    override fun createWork(): Single<Result> {
        return Single.create<Result?> { emitter ->
            updateNotifyInfo()
            val url = inputData.getString(URL_KEY)
            if (url.isNullOrBlank()) {
                emitter.onError(Exception("url is null"))
                return@create
            }
            val build = inputData.getInt(BUILD_KEY, 0).let { if (it < 0) 0 else it }
            if (build == 0) {
                clearTempOtaBuild()
            }
            clearTempDir()

            val file = createTempFile()
            try {
                NetUtil.download(URL(url), file, this)
            } catch (th: Throwable) {
                emitter.onError(th)
                return@create
            }
            if (isStopped) {
                emitter.onSuccess(Result.success())
                return@create
            }

            val ota = getOtaFile(build)
            if (ota.exists()) {
                ota.delete()
            }

            file.copyTo(ota)
            file.delete()

            emitter.onSuccess(createSuccessResult(ota))
        }.subscribeOn(Schedulers.io()).onErrorReturn {
            it.printStackTrace()
            Result.failure()
        }
    }

    private fun clearTempDir() {
        Updater.getTempDir(applicationContext).deleteDir()
    }

    private fun clearTempOtaBuild() {
        val file = getOtaFile(0)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun isDownloadCanceled(): Boolean {
        return isStopped
    }

    private fun createTempFile(): File {
        return Updater.get().createTempFile()
    }

    private fun getOtaFile(build: Int): File {
        return Updater.get().getOtaFile(build)
    }

    companion object {
        const val URL_KEY = "URL"
        const val FILEPATH_KEY = "FILEPATH"
        const val BUILD_KEY = "BUILD"

        private const val MIN_NOTIFY_TIMEOUT = 200

        private fun createSuccessResult(file: File): Result {
            return Result.success(Data.Builder().putString(FILEPATH_KEY, file.path).build())
        }
    }
}
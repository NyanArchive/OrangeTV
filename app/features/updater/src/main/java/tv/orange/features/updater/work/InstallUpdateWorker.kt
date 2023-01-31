package tv.orange.features.updater.work


import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import tv.orange.core.util.PackageHelper
import tv.twitch.android.core.work.AssistedWorkerFactory
import java.io.File


class InstallUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters
) : Worker(
    context,
    workerParams
) {
    @AssistedFactory
    interface Factory : AssistedWorkerFactory<InstallUpdateWorker>
    
    override fun doWork(): Result {
        val filepath: String = inputData.getString(DownloadUpdateRxWorker.FILEPATH_KEY) ?: return Result.failure()

        val apk = File(filepath)
        if (!apk.exists()) {
            return Result.failure()
        }

        PackageHelper.installApk(context = applicationContext, file = apk)

        return Result.success()
    }
}
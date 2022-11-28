package tv.orange.features.updater.data.mvp

import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.component.data.repository.UpdaterRepository
import java.io.File

interface UpdaterContract {
    interface View {
        fun close()
        fun render(state: State)
        fun createTempFile(): File
        fun getOtaFile(build: Int): File
        fun requestInstallPermission()
        fun installApk(file: File)
        fun canInstallApk(): Boolean
        fun saveTextToClipboard(text: String)
        fun clearTempCache()

        sealed class State {
            object Prepare : State()
            object Loading : State()
            data class Loaded(val updateData: UpdateData) : State()
            data class Downloading(val progress: Int, val downloaded: Int, val total: Int) : State()
            object IndeterminateDownloading : State()
            data class DownloadComplete(val file: File) : State()
            data class Error(val msg: String) : State()
        }
    }

    abstract class Presenter(val view: View) {
        abstract fun onCreate()
        abstract fun onDestroy()
        abstract fun onResume()
        abstract fun onPause()
        abstract fun onStart()
        abstract fun onStop()

        abstract fun init(data: UpdateData)

        abstract fun onViewEvent(event: Event)

        sealed class Event {
            object OnCloseClicked : Event()
            object OnCancelClicked : Event()
            object OnActionClicked : Event()
            object OnLongActionClicked : Event()
            object OnPermissionGiven : Event()
            object OnPermissionDenied : Event()
        }

        sealed class State {
            object Starting : State()
            data class Initial(val updateData: UpdateData) : State()
            data class ReadyToDownload(val updateData: UpdateData) : State()
            object StartDownloading: State()
            data class ReadyToInstall(val file: File) : State()
            object StartInstalling: State()
            object CheckPermissions: State()
            object CopyApkUrl : State()

            data class Installing(val file: File): State()

            data class UpdateProgress(val progress: Int, val downloaded: Int, val total: Int): State()
            data class Error(val msg: String): State()
        }
    }
}
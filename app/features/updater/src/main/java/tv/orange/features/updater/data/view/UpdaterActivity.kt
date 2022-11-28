package tv.orange.features.updater.data.view

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.Formatter
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import tv.orange.core.ResourceManager
import tv.orange.core.util.PackageHelper
import tv.orange.core.util.ViewUtil
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.setContentView
import tv.orange.features.updater.Updater
import tv.orange.features.updater.Updater.Companion.getOtaDir
import tv.orange.features.updater.Updater.Companion.getTempDir
import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.data.mvp.UpdaterContract
import tv.orange.features.updater.data.mvp.UpdaterPresenter
import java.io.File

class UpdaterActivity : AppCompatActivity(), UpdaterContract.View {
    private val presenter = UpdaterPresenter(this)

    private lateinit var changelogTv: TextView
    private lateinit var buildTv: TextView
    private lateinit var downloadSizeTv: TextView
    private lateinit var needUpdateTv: TextView
    private lateinit var downloadProgressDataTv: TextView
    private lateinit var downloadProgressProcTv: TextView
    private lateinit var errorMsgTv: TextView

    private lateinit var logoImg: ImageView
    private lateinit var cancelDownloadingImg: ImageView
    private lateinit var closeButtonImg: ImageView

    private lateinit var loadingPb: ProgressBar
    private lateinit var downloadingPb: ProgressBar

    private lateinit var actionButtonBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView("orangetv_updater")

        closeButtonImg = getView<ImageView>("orangetv_updater__close").apply {
            setOnClickListener {
                presenter.onViewEvent(UpdaterContract.Presenter.Event.OnCloseClicked)
            }
        }
        cancelDownloadingImg = getView("orangetv_updater__cancel_downloading")
        logoImg = getView("orangetv_updater__image_logo")

        actionButtonBtn = getView<Button>("orangetv_updater__install_button").apply {
            setOnClickListener {
                presenter.onViewEvent(UpdaterContract.Presenter.Event.OnActionClicked)
            }
            setOnLongClickListener {
                presenter.onViewEvent(UpdaterContract.Presenter.Event.OnLongActionClicked)
                true
            }
        }

        changelogTv = getView("orangetv_updater__changelog")
        buildTv = getView("orangetv_updater__build")
        downloadSizeTv = getView("orangetv_updater__ds")
        downloadProgressDataTv = getView("orangetv_updater__download_progress_label")
        downloadProgressProcTv = getView("orangetv_updater__download_progress_label2")
        needUpdateTv = getView("orangetv_updater__t1")
        errorMsgTv = getView("orangetv_updater__error_msg")

        loadingPb = getView("orangetv_updater__pogress")
        downloadingPb = getView("orangetv_updater__downloading_progress")

        presenter.onCreate()
        presenter.init(intent.getParcelableExtra(EXTRA_UPDATE_DATA)!!)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    companion object {
        const val EXTRA_UPDATE_DATA = "EXTRA_UPDATE_DATA"

        private const val REQUEST_INSTALL_ACTIVITY_CODE = 1

        fun startActivity(context: Context, data: UpdateData) {
            val intent = Intent(context, UpdaterActivity::class.java).apply {
                putExtra(EXTRA_UPDATE_DATA, data)
            }

            context.startActivity(intent)
        }
    }

    override fun close() {
        finishAndRemoveTask()
    }

    private fun hideAllViews() {
        ViewUtil.hide(
            changelogTv,
            buildTv,
            downloadSizeTv,
            needUpdateTv,
            downloadProgressDataTv,
            downloadProgressProcTv,
            errorMsgTv,
            logoImg,
            cancelDownloadingImg,
            loadingPb,
            downloadingPb,
            actionButtonBtn
        )
    }

    override fun render(state: UpdaterContract.View.State) {
        hideAllViews()
        when (state) {
            UpdaterContract.View.State.Prepare -> {}
            UpdaterContract.View.State.Loading -> {
                ViewUtil.show(loadingPb)
            }
            is UpdaterContract.View.State.Loaded -> {
                state.updateData.let { data ->
                    changelogTv.text = data.changelog
                    buildTv.text = data.build.toString()
                    downloadSizeTv.text = ResourceManager.get().getString(
                        "orange_updater_ds", if (data.size > 0) {
                            Formatter.formatFileSize(this, data.size)
                        } else {
                            "Unknown"
                        }
                    )
                }

                state.updateData.logoUrl?.let { url ->
                    Glide.with(logoImg).load(url)
                }

                actionButtonBtn.text = ResourceManager.get().getString(
                    "orange_updater_update_action"
                )

                ViewUtil.show(
                    changelogTv,
                    buildTv,
                    downloadSizeTv,
                    logoImg,
                    actionButtonBtn
                )
            }
            UpdaterContract.View.State.IndeterminateDownloading -> {
                downloadingPb.isIndeterminate = true

                ViewUtil.show(
                    changelogTv,
                    buildTv,
                    downloadSizeTv,
                    logoImg,
                    downloadingPb
                )
            }
            is UpdaterContract.View.State.DownloadComplete -> {
                actionButtonBtn.text = ResourceManager.get().getString(
                    "orange_updater_install_action"
                )

                ViewUtil.show(
                    changelogTv,
                    buildTv,
                    downloadSizeTv,
                    logoImg,
                    actionButtonBtn
                )
            }
            is UpdaterContract.View.State.Error -> {
                errorMsgTv.text = ResourceManager.get().getString(
                    "orange_updater_error_msg",
                    state.msg
                )

                ViewUtil.show(errorMsgTv)
            }
            is UpdaterContract.View.State.Downloading -> {
                downloadingPb.isIndeterminate = false
                downloadingPb.progress = state.progress
                downloadProgressDataTv.text = ResourceManager.get().getString(
                    "orange_updater_ds2",
                    Formatter.formatFileSize(this, state.downloaded.toLong()),
                    Formatter.formatFileSize(this, state.total.toLong())
                )
                downloadProgressProcTv.text = ResourceManager.get().getString(
                    "orange_updater_ds3",
                    state.progress
                )

                ViewUtil.show(
                    changelogTv,
                    buildTv,
                    logoImg,
                    downloadSizeTv,
                    downloadingPb,
                    downloadProgressDataTv,
                    downloadProgressProcTv,
                )
            }
        }
    }

    override fun createTempFile(): File {
        return File(getTempDir(this), "${System.currentTimeMillis()}.tmp")
    }

    override fun getOtaFile(build: Int): File {
        return File(getOtaDir(this), "$build.apk")
    }

    override fun requestInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = Uri.parse("package:${packageName}")
            }
            startActivityForResult(intent, REQUEST_INSTALL_ACTIVITY_CODE)
        }
    }

    override fun installApk(file: File) {
        PackageHelper.installApk(this, file)
    }

    override fun canInstallApk(): Boolean {
        return PackageHelper.canInstallApk(this)
    }

    override fun saveTextToClipboard(text: String) {
        val clip = ClipData.newPlainText("URL", text)
        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
    }

    override fun clearTempCache() {
        Updater.get().clearTempCache(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_INSTALL_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.onViewEvent(UpdaterContract.Presenter.Event.OnPermissionGiven)
            } else {
                presenter.onViewEvent(UpdaterContract.Presenter.Event.OnPermissionDenied)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
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
import tv.orange.core.LoggerImpl
import tv.orange.core.ResourceManager
import tv.orange.core.util.PackageHelper
import tv.orange.core.util.ViewUtil
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.setContentView
import tv.orange.features.updater.Updater.Companion.getOtaDir
import tv.orange.features.updater.Updater.Companion.getTempDir
import tv.orange.features.updater.data.mvp.UpdaterContract
import tv.orange.features.updater.data.mvp.UpdaterPresenter
import java.io.File

class UpdaterActivity : AppCompatActivity(), UpdaterContract.View {
    private val presenter = UpdaterPresenter(this)

    private lateinit var changelogTv: TextView
    private lateinit var buildTv: TextView
    private lateinit var dsTv: TextView
    private lateinit var t1Tv: TextView
    private lateinit var ds2Tv: TextView
    private lateinit var ds3Tv: TextView
    private lateinit var errorMsgTv: TextView

    private lateinit var logo: ImageView

    private lateinit var cancelDownloading: ImageView

    private lateinit var progressBar: ProgressBar
    private lateinit var downloadingProgressBar: ProgressBar
    private lateinit var actionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView("orangetv_updater")
        getView<ImageView>("orangetv_updater__close").setOnClickListener {
            presenter.onViewEvent(UpdaterContract.Presenter.Event.OnCloseClicked)
        }
        actionButton = getView<Button>("orangetv_updater__install_button").apply {
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
        dsTv = getView("orangetv_updater__ds")
        ds2Tv = getView("orangetv_updater__download_progress_label")
        ds3Tv = getView("orangetv_updater__download_progress_label2")
        t1Tv = getView("orangetv_updater__t1")
        errorMsgTv = getView("orangetv_updater__error_msg")

        cancelDownloading = getView("orangetv_updater__cancel_downloading")

        progressBar = getView("orangetv_updater__pogress")
        downloadingProgressBar = getView("orangetv_updater__downloading_progress")
        logo = getView("orangetv_updater__image_logo")

        presenter.onCreate()
        presenter.init(
            intent.getStringExtra(EXTRA_CODENAME),
            intent.getStringExtra(EXTRA_URL)!!,
            intent.getStringExtra(EXTRA_LOGO_URL),
            intent.getIntExtra(EXTRA_BUILD, -1),
            intent.getStringExtra(EXTRA_CHANGELOG)
        )
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

    companion object {
        const val EXTRA_CODENAME = "EXTRA_CODENAME"
        const val EXTRA_URL = "EXTRA_URL"
        const val EXTRA_LOGO_URL = "EXTRA_LOGO_URL"
        const val EXTRA_BUILD = "EXTRA_BUILD"
        const val EXTRA_CHANGELOG = "EXTRA_CHANGELOG"

        const val TEMP_OTA_DIR = "tmp_ota"
        const val INSTALL_OTA_DIR = "install_ota"

        private const val REQUEST_INSTALL_ACTIVITY_CODE = 1

        fun startActivity(
            context: Context,
            codename: String?,
            url: String,
            logoUrl: String?,
            build: Int?,
            changelog: String?
        ) {
            context.startActivity(Intent(context, UpdaterActivity::class.java).apply {
                putExtra(EXTRA_CODENAME, codename)
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_LOGO_URL, logoUrl)
                putExtra(EXTRA_BUILD, build)
                putExtra(EXTRA_CHANGELOG, changelog)
            })
        }
    }

    override fun close() {
        finishAndRemoveTask()
    }

    override fun render(state: UpdaterContract.View.State) {
        LoggerImpl.debug("state: $state")
        when (state) {
            UpdaterContract.View.State.Prepare -> {
                ViewUtil.hide(
                    progressBar,
                    changelogTv,
                    buildTv,
                    dsTv,
                    actionButton,
                    t1Tv,
                    logo,
                    downloadingProgressBar,
                    cancelDownloading,
                    ds2Tv,
                    ds3Tv
                )
            }
            UpdaterContract.View.State.Loading -> {
                ViewUtil.show(progressBar)
            }
            is UpdaterContract.View.State.Loaded -> {
                ViewUtil.hide(progressBar)

                state.updateData.let { data ->
                    changelogTv.text = data.changelog
                    buildTv.text = data.build.toString()
                    dsTv.text = ResourceManager.get().getString(
                        "orange_updater_ds", if (data.size > 0) {
                            Formatter.formatFileSize(this, data.size)
                        } else {
                            "Unknown"
                        }
                    )
                }

                state.updateData.logoUrl?.let { url ->
                    Glide.with(logo).load(url)
                }

                ViewUtil.show(changelogTv, buildTv, dsTv, actionButton, t1Tv, logo)
            }
            UpdaterContract.View.State.IndeterminateDownloading -> {
                downloadingProgressBar.isIndeterminate = true

                ViewUtil.show(downloadingProgressBar)
                ViewUtil.hide(actionButton, t1Tv, dsTv)
            }
            is UpdaterContract.View.State.DownloadComplete -> {
                ViewUtil.show(actionButton)
                ViewUtil.hide(downloadingProgressBar, ds2Tv, ds3Tv)

                actionButton.text = "Install"
            }
            is UpdaterContract.View.State.Error -> {
                ViewUtil.hide(
                    changelogTv,
                    buildTv,
                    dsTv,
                    t1Tv,
                    ds2Tv,
                    ds3Tv,
                    logo,
                    cancelDownloading,
                    progressBar,
                    downloadingProgressBar,
                    actionButton
                )
                ViewUtil.show(errorMsgTv)
                errorMsgTv.text =
                    ResourceManager.get().getString("orange_updater_error_msg", state.msg)
            }
            is UpdaterContract.View.State.Downloading -> {
                downloadingProgressBar.isIndeterminate = false
                downloadingProgressBar.progress = state.progress
                ds2Tv.text = ResourceManager.get().getString(
                    "orange_updater_ds2",
                    Formatter.formatFileSize(this, state.downloaded.toLong()),
                    Formatter.formatFileSize(this, state.total.toLong())
                )
                ds3Tv.text = ResourceManager.get().getString("orange_updater_ds3", state.progress)

                ViewUtil.show(downloadingProgressBar, ds2Tv, ds3Tv)
                ViewUtil.hide(actionButton)
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
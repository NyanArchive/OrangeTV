package tv.orange.features.updater.data.mvp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tv.orange.core.Logger
import tv.orange.core.util.FileUtil.copyTo
import tv.orange.core.util.NetUtil
import tv.orange.core.util.NetUtil.getFileSize
import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.data.view.UpdaterActivity
import java.io.File
import java.net.URL
import java.util.concurrent.Executors

class UpdaterPresenter(
    view: UpdaterActivity
) : UpdaterContract.Presenter(view) {
    private lateinit var updateData: UpdateData
    private lateinit var installFile: File

    private val disposables = CompositeDisposable()
    private lateinit var observerDisposable: Disposable
    private val subject = BehaviorSubject.create<State>()

    private var readyToInstall = false

    private fun observerStates() {
        observerDisposable =
            subject.subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state ->
                    Logger.debug("state: $state")
                    when (state) {
                        State.Starting -> {
                            view.render(UpdaterContract.View.State.Prepare)
                        }
                        is State.Update -> {
                            view.render(UpdaterContract.View.State.Loading)
                            tryUpdateInfo(state)
                        }
                        is State.ReadyToDownload -> {
                            updateData = state.updateData
                            view.render(UpdaterContract.View.State.Loaded(state.updateData))
                        }
                        State.StartDownloading -> {
                            view.render(UpdaterContract.View.State.IndeterminateDownloading)
                            tryDownloadFile(updateData)
                        }
                        is State.ReadyToInstall -> {
                            view.render(UpdaterContract.View.State.DownloadComplete(state.file))
                            installFile = state.file
                            readyToInstall = true
                        }
                        State.StartInstalling -> {
                            view.render(UpdaterContract.View.State.IndeterminateDownloading)
                            if (view.canInstallApk()) {
                                subject.onNext(State.Installing(installFile))
                            } else {
                                subject.onNext(State.Error("Permissions"))
                            }
                        }
                        State.CheckPermissions -> {
                            if (view.canInstallApk()) {
                                subject.onNext(State.StartDownloading)
                            } else {
                                view.requestInstallPermission()
                            }
                        }
                        is State.Installing -> {
                            view.render(UpdaterContract.View.State.IndeterminateDownloading)
                            view.installApk(state.file)
                        }
                        is State.Error -> {
                            view.render(UpdaterContract.View.State.Error(state.msg))
                        }
                        is State.UpdateProgress -> {
                            view.render(
                                UpdaterContract.View.State.Downloading(
                                    state.progress,
                                    state.downloaded,
                                    state.total
                                )
                            )
                        }
                    }
                }, {
                    subject.onNext(State.Error(it.localizedMessage ?: "Throwable"))
                })
    }

    override fun onViewEvent(event: Event) {
        Logger.debug("event: $event")
        when (event) {
            Event.OnCancelClicked -> {
                view.close()
            }
            Event.OnCloseClicked -> {
                view.close()
            }
            Event.OnActionClicked -> {
                view.render(UpdaterContract.View.State.IndeterminateDownloading)
                if (readyToInstall) {
                    subject.onNext(State.StartInstalling)
                } else {
                    subject.onNext(State.CheckPermissions)
                }
            }
            Event.OnPermissionGiven -> {
                subject.onNext(State.StartDownloading)
            }
            Event.OnPermissionDenied -> {
                subject.onNext(State.Error("Permissions"))
            }
        }
    }

    override fun onCreate() {
        observerStates()
    }

    override fun onDestroy() {
        disposables.clear()
        observerDisposable.dispose()
    }

    override fun onResume() {
        subject.onNext(State.Starting)
        subject.onNext(State.Update(updateData))
    }

    override fun onPause() {
        disposables.clear()
    }

    override fun init(
        codename: String?,
        url: String,
        logoUrl: String?,
        build: Int,
        changelog: String?
    ) {
        updateData = UpdateData(
            build = build,
            codename = codename ?: "Unknown",
            url = url,
            logoUrl = logoUrl,
            changelog = changelog
        )
    }

    private fun tryUpdateInfo(state: State.Update) {
        disposables.add(
            Single.create { e ->
                e.onSuccess(
                    updateData.copy(size = getFileSize(updateData.url))
                )
            }.subscribeOn(Schedulers.io()).subscribe({ data ->
                updateData = data
                subject.onNext(State.ReadyToDownload(data))
            }, {
                subject.onNext(State.Error(it.localizedMessage ?: "Throwable"))
            })
        )
    }

    private fun tryDownloadFile(updateData: UpdateData) {
        disposables.add(
            downloadFile(updateData)
                .subscribeOn(Schedulers.io()).subscribe(
                    {
                        subject.onNext(State.ReadyToInstall(it))
                    },
                    {
                        subject.onNext(State.Error(it.localizedMessage ?: "Throwable"))
                    }
                )
        )
    }

    private fun downloadFile(data: UpdateData): Single<File> {
        return Single.create { e ->
            val build = if (data.build > 0) data.build else 0

            val tmpApkFile = view.createTempFile()
            val otaFile = view.getOtaFile(build)

            try {
                NetUtil.download(URL(data.url), tmpApkFile, object : NetUtil.DownloadCallback {
                    override fun onProgressUpdate(
                        progress: Int,
                        downloadedBytes: Int,
                        totalBytes: Int
                    ) {
                        subject.onNext(State.UpdateProgress(progress, downloadedBytes, totalBytes))
                    }

                    override fun isCanceled(): Boolean {
                        return e.isDisposed
                    }
                })

                tmpApkFile.copyTo(otaFile)

                if (e.isDisposed) {
                    otaFile.delete()
                } else {
                    e.onSuccess(otaFile)
                }
            } catch (th: Throwable) {
                subject.onNext(State.Error(th.localizedMessage ?: "Throwable"))
            } finally {
                tmpApkFile.delete()
            }
        }
    }
}
package tv.orange.features.updater.data.mvp

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import tv.orange.core.util.NetUtil.getFileSize
import tv.orange.features.updater.Updater
import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.features.updater.data.view.UpdaterActivity
import java.io.File
import java.util.concurrent.Executors

class UpdaterPresenter(
    view: UpdaterActivity
) : UpdaterContract.Presenter(view) {
    private lateinit var updateData: UpdateData
    private lateinit var installFile: File

    private val disposables = CompositeDisposable()
    private val subject = BehaviorSubject.create<State>()

    private lateinit var observer: Disposable

    private var readyToInstall = false

    override fun onViewEvent(event: Event) {
        when (event) {
            Event.OnCancelClicked -> {
                view.close()
            }
            Event.OnCloseClicked -> {
                view.close()
            }
            Event.OnDiscordClicked -> {
                view.openDiscord()
            }
            Event.OnTgClicked -> {
                view.openTg()
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
            Event.OnLongActionClicked -> {
                subject.onNext(State.CopyApkUrl)
            }
        }
    }

    override fun onCreate() {
        observer = subject.subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                when (state) {
                    State.Starting -> {
                        view.render(UpdaterContract.View.State.Prepare)
                    }
                    is State.Initial -> {
                        tryUpdateInfo(state)
                    }
                    is State.ReadyToDownload -> {
                        updateData = state.updateData
                        view.render(UpdaterContract.View.State.Loaded(updateData))
                    }
                    State.StartDownloading -> {
                        tryDownloadFile(updateData)
                        view.render(UpdaterContract.View.State.IndeterminateDownloading)
                    }
                    is State.ReadyToInstall -> {
                        readyToInstall = true
                        installFile = state.file
                        if (state.updateFileSize) {
                            updateData = updateData.copy(size = installFile.length())
                        }
                        view.render(
                            UpdaterContract.View.State.DownloadComplete(
                                file = installFile,
                                data = updateData
                            )
                        )
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
                    State.CopyApkUrl -> {
                        view.saveTextToClipboard(updateData.url)
                    }
                }
            }, {
                subject.onNext(State.Error(it.localizedMessage ?: "Throwable"))
            })
    }

    override fun onDestroy() {
        disposables.clear()
        observer.dispose()
    }

    override fun onResume() {}
    override fun onPause() {}

    override fun onStart() {
        subject.onNext(State.Starting)
        val file = view.getOtaFile(updateData.build)
        if (!file.exists()) {
            subject.onNext(State.Initial(updateData))
        } else {
            subject.onNext(State.ReadyToInstall(file, true))
        }
    }

    override fun onStop() {
        disposables.clear()
    }

    override fun init(data: UpdateData) {
        updateData = data
    }

    private fun tryUpdateInfo(state: State.Initial) {
        disposables.add(
            Single.create { e ->
                e.onSuccess(
                    updateData.copy(size = getFileSize(updateData.url))
                )
            }.subscribeOn(Schedulers.io()).subscribe({ data ->
                updateData = data
                subject.onNext(State.ReadyToDownload(data))
            }, {
                it.printStackTrace()
                subject.onNext(State.Error(it.localizedMessage ?: "Throwable"))
            })
        )
    }

    private fun tryDownloadFile(updateData: UpdateData) {
        Updater.get().scheduleWork(
            url = updateData.url,
            build = updateData.build
        )
    }
}
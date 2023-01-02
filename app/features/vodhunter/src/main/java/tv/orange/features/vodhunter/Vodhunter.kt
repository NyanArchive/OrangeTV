package tv.orange.features.vodhunter

import android.content.Context
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.ResourceManager
import tv.orange.core.compat.ClassCompat
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.util.NetUtil
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.vodhunter.bridge.ISharePanelWidgetViewDelegate
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.viewdelegate.RxViewDelegate
import tv.twitch.android.models.clips.ClipQualityOption
import tv.twitch.android.shared.share.panel.SharePanelDefaultPresenter
import tv.twitch.android.shared.ui.elements.bottomsheet.InteractiveRowView
import javax.inject.Inject

class Vodhunter @Inject constructor(
    val nopRepository: NopRepository,
    val rm: ResourceManager,
    val context: Context
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Vodhunter::class.java)

        @JvmStatic
        val isEnabled: Boolean
            get() = Flag.VODHUNTER.asBoolean()
    }

    fun maybeHookVodManifestResponse(
        vodResponse: Single<Response<String>>,
        vodName: String
    ): Single<Response<String>> {
        if (!Flag.VODHUNTER.asBoolean()) {
            return vodResponse
        }

        return vodResponse.flatMap {
            when {
                it.isSuccessful -> return@flatMap vodResponse
                it.code() == 403 -> {
                    return@flatMap createSubFreePlaylist(
                        Single.just(it),
                        vodName
                    )
                }
                else -> vodResponse
            }
        }
    }

    private fun createSubFreePlaylist(
        orgResponse: Single<Response<String>>,
        vodId: String
    ): Single<Response<String>> {
        return vodId.toIntOrNull()?.let { id ->
            nopRepository.getVodhunterPlaylist(vodId = id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    Core.showToast("[VODHunter] ${rm.getString("orange_vodhunter_hunting")}")
                }
                .onErrorResumeNext { th: Throwable ->
                    Core.showToast(
                        rm.getString(
                            "orange_generic_error_d",
                            "VODHunter",
                            th.localizedMessage ?: "<empty>"
                        )
                    )
                    th.printStackTrace()
                    orgResponse
                }
        } ?: run {
            orgResponse
        }
    }

    fun getDownloadClipButton(delegate: RxViewDelegate<*, *>): InteractiveRowView {
        return delegate.contentView.getView<InteractiveRowView>(resName = "share_panel_widget__download_clip")
            .apply {
                ClassCompat.invokeIf<ISharePanelWidgetViewDelegate>(obj = delegate) { proxy ->
                    setOnClickListener {
                        proxy.pushDownloadClipEvent()
                    }
                }
            }
    }

    fun tryDownloadClip(
        presenter: SharePanelDefaultPresenter,
        state: SharePanelDefaultPresenter.State.Initialized
    ) {
        val playable = state.sharePlayableModel ?: run {
            LoggerImpl.error("sharePlayableModel is null")
            return
        }

        if (playable is SharePanelDefaultPresenter.SharePlayable.Clip) {
            val model = playable.clipModel
            NetUtil.downloadClipLegacy(
                context = context,
                url = model.qualityOptions.first().source,
                filename = model.title.removeSuffix(".")
            )
        }
    }

    override fun onCreateFeature() {}
}
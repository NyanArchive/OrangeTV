package tv.orange.features.vodhunter

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.models.abc.Feature
import javax.inject.Inject

class Vodhunter @Inject constructor(
    val nopRepository: NopRepository,
    val rm: ResourceManager
) : Feature {
    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}

    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Vodhunter::class.java)
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
                    Core.toast("[VODHunter] ${rm.getString("orange_vodhunter_hunting")}")
                }
                .onErrorResumeNext { th: Throwable ->
                    Core.toast(
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
}
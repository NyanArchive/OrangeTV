package tv.orange.features.proxy

import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.ProxyImpl
import tv.orange.features.api.component.repository.ProxyRepository
import tv.orange.models.abc.Feature
import tv.twitch.android.models.AccessTokenResponse
import tv.twitch.android.models.manifest.extm3u
import javax.inject.Inject

class Proxy @Inject constructor(
    val repository: ProxyRepository,
    val rm: ResourceManager
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Proxy::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(Proxy::class.java)
        }

        @JvmStatic
        fun tryHookStreamManifestResponse(
            orgStreamManifest: Single<Response<String>>,
            streamName: String,
            accessTokenResponse: AccessTokenResponse
        ): Single<Response<String>> {
            return get().maybeHookStreamManifestResponse(
                orgStreamManifest,
                streamName,
                accessTokenResponse
            )
        }

        private fun createPlaylistResponse(
            playlist: String, orgResponse: Response<String>
        ): Response<String> {
            val patchedResponse = orgResponse.raw().newBuilder().apply {
                body(playlist.toResponseBody("application/vnd.apple.mpegurl".toMediaType()))
            }.build()

            return Response.success(playlist, patchedResponse)
        }

        private fun getRequestTime(response: okhttp3.Response): Int = try {
            val tx = response.sentRequestAtMillis
            val rx = response.receivedResponseAtMillis
            (rx - tx).toInt()
        } catch (th: Throwable) {
            0
        }

        private fun trySwapPlaylist(
            twitchResponse: Single<Response<String>>,
            proxyResponse: Maybe<Response<String>>,
            proxyName: String
        ): Single<Response<String>> {
            return twitchResponse.zipWith(
                proxyResponse.toSingle()
            ) { twitchPlaylist: Response<String>, proxyPlaylist: Response<String> ->
                if (!proxyPlaylist.isSuccessful) {
                    Core.toast(
                        ResourceManager.get().getString(
                            "orange_generic_error_d",
                            "Proxy",
                            ResourceManager.get().getString("orange_proxy_error_ur")
                        )
                    )
                    return@zipWith twitchPlaylist
                }
                val time = getRequestTime(proxyPlaylist.raw())
                var body = proxyPlaylist.body() ?: run {
                    Core.toast(
                        ResourceManager.get().getString(
                            "orange_generic_error_d",
                            "Proxy",
                            ResourceManager.get().getString("orange_proxy_error_cpr")
                        )
                    )
                    return@zipWith twitchPlaylist
                }
                val proxyUrl = proxyPlaylist.raw().request.url
                body = body.replace(
                    "#EXT-X-TWITCH-INFO:",
                    "#EXT-X-TWITCH-INFO:PROXY-SERVER=\"$proxyName ($time ms)\",PROXY-URL=\"$proxyUrl\","
                )

                createPlaylistResponse(body, twitchPlaylist)
            }.onErrorResumeNext { th: Throwable ->
                Core.toast(
                    ResourceManager.get().getString(
                        "orange_generic_error_d",
                        "Proxy",
                        th.localizedMessage ?: "<empty>"
                    )
                )
                if (th !is NullPointerException) {
                    th.printStackTrace()
                }
                twitchResponse
            }
        }

        @JvmStatic
        fun hookPlaylistUrl(requestUrl: String, model: extm3u): String {
            if (model.ProxyUrl.isNullOrBlank()) {
                return requestUrl
            }

            return model.ProxyUrl
        }
    }

    fun createTTSFTProxySingleResponse(
        twitchResponse: Single<Response<String>>,
        channelName: String,
        accessTokenResponse: AccessTokenResponse
    ): Single<Response<String>> {
        return trySwapPlaylist(
            twitchResponse = twitchResponse, proxyResponse = repository.getTTSFTPlaylist(
                channelName = channelName,
                sig = accessTokenResponse.sig,
                token = accessTokenResponse.token
            ), proxyName = "Twitch Tokyo Server Fix Tool"
        )
    }

    fun createTwitchingProxySingleResponse(
        twitchResponse: Single<Response<String>>,
        channelName: String,
        accessTokenResponse: AccessTokenResponse
    ): Single<Response<String>> {
        return trySwapPlaylist(
            twitchResponse = twitchResponse, proxyResponse = repository.getTwitchingPlaylist(
                channelName = channelName,
                sig = accessTokenResponse.sig,
                token = accessTokenResponse.token
            ), proxyName = "Twitching"
        )
    }

    fun maybeHookStreamManifestResponse(
        manifest: Single<Response<String>>,
        channelName: String,
        accessTokenResponse: AccessTokenResponse
    ): Single<Response<String>> {
        if (Flag.Proxy.asVariant<ProxyImpl>() == ProxyImpl.Disabled) {
            return manifest
        }

        return when (Flag.Proxy.asVariant<ProxyImpl>()) {
            ProxyImpl.Twitching -> createTwitchingProxySingleResponse(
                manifest, channelName, accessTokenResponse
            )
            ProxyImpl.TTSFTP -> createTTSFTProxySingleResponse(
                manifest, channelName, accessTokenResponse
            )
            else -> manifest
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
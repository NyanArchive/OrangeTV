package tv.orange.features.proxy

import com.google.android.exoplayer2.upstream.DataSpec
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import tv.orange.core.Core
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.ProxyImpl
import tv.orange.features.api.component.repository.ProxyRepository
import tv.orange.features.proxy.bridge.LolTvApiInterceptor
import tv.orange.features.proxy.bridge.PatchKoreaHlsRequestInterceptor
import tv.orange.features.proxy.bridge.PatchSegmentNodeRequestInterceptor
import tv.orange.models.abc.Feature
import tv.twitch.android.models.AccessTokenResponse
import tv.twitch.android.models.manifest.extm3u
import java.util.*
import javax.inject.Inject

class Proxy @Inject constructor(
    val repository: ProxyRepository
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Proxy::class.java)

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
            proxyResponse: Single<Response<String>>,
            proxyName: String
        ): Single<Response<String>> {
            val rm = ResourcesManagerCore.get()
            return proxyResponse.flatMap { proxyPlaylist ->
                if (!proxyPlaylist.isSuccessful) {
                    Core.showToast(
                        ResourcesManagerCore.get().getString(
                            "orange_generic_error_d",
                            "Proxy",
                            rm.getString("orange_proxy_error_ur")
                        )
                    )
                    return@flatMap Single.error(Exception("proxy_unsuccessfull"))
                }
                val time = getRequestTime(proxyPlaylist.raw())
                var body = proxyPlaylist.body() ?: run {
                    Core.showToast(
                        rm.getString(
                            "orange_generic_error_d",
                            "Proxy",
                            rm.getString("orange_proxy_error_cpr")
                        )
                    )
                    return@flatMap Single.error(Exception("proxy_error"))
                }
                val proxyUrl = proxyPlaylist.raw().request.url
                body = body.replace(
                    "#EXT-X-TWITCH-INFO:",
                    "#EXT-X-TWITCH-INFO:PROXY-SERVER=\"$proxyName ($time ms)\",PROXY-URL=\"$proxyUrl\","
                )

                Single.just(createPlaylistResponse(body, proxyPlaylist))
            }.onErrorResumeNext { th: Throwable ->
                Core.showToast(
                    rm.getString(
                        "orange_generic_error_d",
                        "Proxy",
                        th.localizedMessage ?: "<empty>"
                    )
                )
                th.printStackTrace()
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

        @JvmStatic
        fun patchExoPlayerDataSpec(dataSpec: DataSpec?): DataSpec? {
            dataSpec ?: run {
                return null
            }

            val url = dataSpec.uri.toString()
            val headers = when {
                url.contains("usher.ttvnw.net/api/channel/hls/") -> {
                    if (Flag.FIX_KOREA_1080P.asBoolean()) {
                        Collections.unmodifiableMap(
                            dataSpec.httpRequestHeaders.toMutableMap().apply {
                                put("X-Forwarded-For", "::1")
                            })
                    } else {
                        null
                    }
                }
                url.contains("https://api.ttv.lol/") -> Collections.unmodifiableMap(
                    dataSpec.httpRequestHeaders.toMutableMap().apply {
                        put("x-donate-to", "https://ttv.lol/donate")
                    })
                url.contains(".ttvnw.net/v1/playlist/") -> mapOf("Accept" to "application/x-mpegURL, application/vnd.apple.mpegurl, application/json, text/plain")
                else -> null
            } ?: return dataSpec

            return DataSpec(
                dataSpec.uri,
                dataSpec.httpMethod,
                dataSpec.httpBody,
                dataSpec.absoluteStreamPosition,
                dataSpec.position,
                dataSpec.length,
                dataSpec.key,
                dataSpec.flags,
                headers
            )
        }

        @JvmStatic
        fun maybeAddInterceptor(builder: OkHttpClient.Builder) {
            builder.addNetworkInterceptor(LolTvApiInterceptor())
            if (Flag.FIX_KOREA_1080P.asBoolean()) {
                builder.addNetworkInterceptor(PatchKoreaHlsRequestInterceptor())
            }
            builder.addNetworkInterceptor(PatchSegmentNodeRequestInterceptor())
        }
    }

    private fun createLolProxySingleResponse(
        twitchResponse: Single<Response<String>>,
        channelName: String
    ): Single<Response<String>> {
        return trySwapPlaylist(
            twitchResponse = twitchResponse,
            proxyResponse = repository.getLolPlaylist(channelName = channelName),
            proxyName = "TTV LOL"
        )
    }

    private fun createTwitchingProxySingleResponse(
        twitchResponse: Single<Response<String>>,
        channelName: String,
        accessTokenResponse: AccessTokenResponse
    ): Single<Response<String>> {
        return trySwapPlaylist(
            twitchResponse = twitchResponse,
            proxyResponse = repository.getTwitchingPlaylist(
                channelName = channelName,
                sig = accessTokenResponse.sig,
                token = accessTokenResponse.token
            ),
            proxyName = "Twitching"
        )
    }

    private fun createPurpleProxySingleResponse(
        twitchResponse: Single<Response<String>>,
        channelName: String
    ): Single<Response<String>> {
        return trySwapPlaylist(
            twitchResponse = twitchResponse,
            proxyResponse = repository.getPurplePlaylist(channelName = channelName),
            proxyName = "Purple Adblock"
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
                twitchResponse = manifest,
                channelName = channelName,
                accessTokenResponse = accessTokenResponse
            )
            ProxyImpl.TTV_LOL -> createLolProxySingleResponse(
                twitchResponse = manifest,
                channelName = channelName
            )
            ProxyImpl.PURPLE -> createPurpleProxySingleResponse(
                twitchResponse = manifest,
                channelName = channelName
            )
            else -> manifest
        }
    }

    override fun onCreateFeature() {}
}
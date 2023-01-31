package tv.orange.features.proxy.bridge

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import tv.orange.core.LoggerImpl
import java.util.regex.Pattern

open class BaseInterceptor(
    private val headers: Map<String, String> = emptyMap(),
    private val params: Map<String, String> = emptyMap(),
    private var pattern: Pattern? = null
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val pattern = getPattern() ?: return chain.proceed(chain.request())
        val res = pattern.matcher(chain.request().url.toString())

        if (!res.find()) {
            return chain.proceed(chain.request())
        }
        LoggerImpl.debug("URL: " + chain.request().url)

        var request = chain.request().newBuilder()
        val headers = getHeaders()
        if (headers.isNotEmpty()) {
            for (entry in headers.entries) {
                request = replaceOrAddHeader(
                    request = request,
                    name = entry.key,
                    value = entry.value
                )
            }
        }
        val params = getParams()
        if (params.isNotEmpty()) {
            var url = chain.request().url
            for (entry in params.entries) {
                url = replaceOrAddParam(
                    url = url,
                    key = entry.key,
                    value = entry.value
                )
            }
            request.url(url)
        }

        return chain.proceed(request.build())
    }

    open fun getPattern(): Pattern? {
        return pattern
    }

    open fun getParams(): Map<String, String> {
        return params
    }

    open fun getHeaders(): Map<String, String> {
        return headers
    }

    companion object {
        private fun replaceOrAddHeader(
            request: Request.Builder,
            name: String,
            value: String
        ): Request.Builder {
            return request.apply {
                removeHeader(name)
                addHeader(name, value)
            }
        }

        private fun replaceOrAddParam(
            url: HttpUrl,
            key: String,
            value: String
        ): HttpUrl {
            return url.newBuilder().apply {
                removeAllQueryParameters(key)
                addQueryParameter(key, value)
            }.build()
        }
    }
}
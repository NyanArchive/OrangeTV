package tv.orange.features.proxy.bridge

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class LolTvApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().url.host.contains("api.ttv.lol")) {
            val request = chain.request().newBuilder()
            replaceHeader(
                request = request,
                name = "x-donate-to",
                value = "https://ttv.lol/donate"
            )
            return chain.proceed(request.build())
        }

        return chain.proceed(chain.request())
    }

    companion object {
        private fun replaceHeader(
            request: Request.Builder,
            name: String,
            value: String
        ) {
            request.removeHeader(name)
            request.addHeader(name, value)
        }
    }
}
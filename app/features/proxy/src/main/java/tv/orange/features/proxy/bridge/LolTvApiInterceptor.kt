package tv.orange.features.proxy.bridge

import java.util.regex.Pattern

class LolTvApiInterceptor : BaseInterceptor(
    headers = mapOf("x-donate-to" to "https://ttv.lol/donate"),
    pattern = Pattern.compile("api\\.ttv\\.lol")
)
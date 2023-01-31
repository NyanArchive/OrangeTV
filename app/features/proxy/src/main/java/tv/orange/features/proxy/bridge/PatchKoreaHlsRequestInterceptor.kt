package tv.orange.features.proxy.bridge

import java.util.regex.Pattern

class PatchKoreaHlsRequestInterceptor : BaseInterceptor(
    headers = mapOf("X-Forwarded-For" to "::1"),
    params = mapOf("force_segment_node" to "akamai_korea"),
    pattern = Pattern.compile("usher\\.ttvnw\\.net/api/channel/hls/")
)
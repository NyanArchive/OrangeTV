package tv.orange.features.proxy.bridge

import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.Node
import java.util.regex.Pattern

class PatchSegmentNodeRequestInterceptor : BaseInterceptor(
    pattern = Pattern.compile("usher\\.ttvnw\\.net/api/channel/hls/")
) {
    override fun getParams(): Map<String, String> {
        return when (val node = Flag.NODE_SEGMENT.asVariant<Node>()) {
            Node.Auto -> emptyMap()
            else -> mapOf("force_segment_node" to node.value)
        }
    }
}
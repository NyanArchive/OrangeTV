package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class Node(val value: String) : Variant {
    Auto("auto"),
    LimelightKt("limelight_kt"),
    LimelightSk("limelight_sk"),
    LimelightLg("limelight_lg"),
    AkamaiKorea("akamai_korea"),
    AkamaiNa("akamai_na");

    override fun getDefault(): Variant {
        return Auto
    }

    override fun toString(): String {
        return value
    }
}
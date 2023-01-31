package tv.orange.core.models.flag.variants

import tv.orange.core.models.flag.core.Variant

enum class Node(val value: String) : Variant {
    Auto("auto"),
    L3_HLS("l3_hls"),
    A_ASIA("akamai_asia"),
    A_KOREA("akamai_korea"),
    A_EU("akamai_eu"),
    A_NA("akamai_na"),
    A_LATAM("akamai_latam"),
    E_ASIA("edgecast_asia"),
    E_EU("edgecast_eu"),
    E_NA("edgecast_na"),
    E_LATAM("edgecast_latam"),
    F_NA("fastly_na"),
    F_EU("fastly_eu"),
    F_APAC("fastly_apac"),
    F_OCEANIA("fastly_oceania"),
    F_LATAM("fastly_latam"),
    C_KOREA("cloudfront_korea"),
    C_GLOBAL("cloudfront_global"),
    L_KT("limelight_kt"),
    L_LG("limelight_lg"),
    L_SK("limelight_sk");

    override fun getDefault(): Variant {
        return Auto
    }

    override fun toString(): String {
        return value
    }
}
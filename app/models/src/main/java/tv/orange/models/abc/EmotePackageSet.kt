package tv.orange.models.abc

enum class EmotePackageSet(
    val value: String,
    val resName: String,
    val emoteType: OrangeEmoteType,
    val isGlobal: Boolean
) {
    Unknown(
        value = "UNKNOWN",
        resName = "orange_emotecard_unknown",
        emoteType = OrangeEmoteType.UNKNOWN,
        isGlobal = false
    ),
    TwitchChannel(
        value = "TWITCH-CHANNEL",
        resName = "orange_emotecard_twitch_channel",
        emoteType = OrangeEmoteType.TWITCH,
        isGlobal = false
    ),
    BttvChannel(
        value = "BTTV-CHANNEL",
        resName = "orange_emotecard_bttv_channel",
        emoteType = OrangeEmoteType.BTTV,
        isGlobal = false
    ),
    FfzChannel(
        value = "FFZ-CHANNEL",
        resName = "orange_emotecard_ffz_channel",
        emoteType = OrangeEmoteType.FFZ,
        isGlobal = false
    ),
    StvChannel(
        value = "7TV-CHANNEL",
        resName = "orange_emotecard_stv_channel",
        emoteType = OrangeEmoteType.STV,
        isGlobal = false
    ),
    TwitchGlobal(
        value = "TWITCH-CHANNEL",
        "orange_emotecard_twitch_global",
        emoteType = OrangeEmoteType.TWITCH,
        isGlobal = true
    ),
    BttvGlobal(
        value = "BTTV-GLOBAL",
        resName = "orange_emotecard_bttv_global",
        emoteType = OrangeEmoteType.BTTV,
        isGlobal = true
    ),
    FfzGlobal(
        value = "FFZ-GLOBAL",
        resName = "orange_emotecard_ffz_global",
        emoteType = OrangeEmoteType.FFZ,
        isGlobal = true
    ),
    StvGlobal(
        value = "7TV-GLOBAL",
        resName = "orange_emotecard_stv_global",
        emoteType = OrangeEmoteType.STV,
        isGlobal = true
    );

    companion object {
        fun from(name: String): EmotePackageSet {
            return values().firstOrNull { it.name == name } ?: Unknown
        }
    }
}
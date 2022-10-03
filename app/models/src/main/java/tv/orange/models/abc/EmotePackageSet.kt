package tv.orange.models.abc

enum class EmotePackageSet(
    val value: String,
    val resName: String,
    val type: OrangeEmoteType,
    val isGlobal: Boolean
) {
    TwitchChannel("TWITCH-CHANNEL", "orange_emotecard_twitch_channel", OrangeEmoteType.TWITCH, false),
    BttvChannel("BTTV-CHANNEL", "orange_emotecard_bttv_channel", OrangeEmoteType.BTTV, false),
    FfzChannel("FFZ-CHANNEL", "orange_emotecard_ffz_channel", OrangeEmoteType.FFZ, false),
    StvChannel("7TV-CHANNEL", "orange_emotecard_stv_channel", OrangeEmoteType.STV, false),
    TwitchGlobal("TWITCH-CHANNEL", "orange_emotecard_twitch_global", OrangeEmoteType.TWITCH, true),
    BttvGlobal("BTTV-GLOBAL", "orange_emotecard_bttv_global", OrangeEmoteType.BTTV, true),
    FfzGlobal("FFZ-GLOBAL", "orange_emotecard_ffz_global", OrangeEmoteType.FFZ, true),
    StvGlobal("7TV-GLOBAL", "orange_emotecard_stv_global", OrangeEmoteType.STV, true)
}
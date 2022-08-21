package tv.orange.models.abc

enum class EmotePackageSet(val value: String, val resName: String) {
    BttvChannel("BTTV-CHANNEL", "orange_emotecard_bttv_channel"),
    FfzChannel("FFZ-CHANNEL", "orange_emotecard_ffz_channel"),
    StvChannel("7TV-CHANNEL", "orange_emotecard_stv_channel"),
    BttvGlobal("BTTV-GLOBAL", "orange_emotecard_bttv_global"),
    FfzGlobal("FFZ-GLOBAL", "orange_emotecard_ffz_global"),
    StvGlobal("7TV-GLOBAL", "orange_emotecard_stv_global")
}
package tv.orange.models.abs

enum class EmotePackageSet(val value: String, val stringId: String) {
    BttvChannel("BTTV-CHANNEL", "orange_emotecard_bttv_channel"),
    FfzChannel("FFZ-CHANNEL", "orange_emotecard_ffz_channel"),
    StvChannel("STV-CHANNEL", "orange_emotecard_stv_channel"),
    BttvGlobal("BTTV-GLOBAL", "orange_emotecard_bttv_global"),
    FfzGlobal("FFZ-GLOBAL", "orange_emotecard_ffz_global"),
    StvGlobal("STV-GLOBAL", "orange_emotecard_stv_global")
}
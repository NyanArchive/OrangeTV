package tv.orange.features.api.di.scope

import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ApiScope

const val BTTV = "bttv"
const val ITZ = "itz"
const val CHATTERINO = "chatterino"
const val NOP = "nop"
const val TTSFT = "ttsft"
const val TWITCHING = "twitching"
const val TTVLOL = "ttvlol"
const val PAS1 = "pas1"
const val PAS2 = "pas2"
const val FFZAP = "ffzap"
const val FFZ = "ffz"
const val STV = "stv"
const val STV_OLD_API = "stv_old"

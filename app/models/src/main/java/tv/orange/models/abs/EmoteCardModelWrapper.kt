package tv.orange.models.abs

import org.json.JSONObject

data class EmoteCardModelWrapper(val token: String, val url: String, val set: EmotePackageSet) {
    fun toJsonString(): String {
        return JSONObject().apply {
            put(JSON_TOKEN_NAME, token)
            put(JSON_URL_NAME, url)
            put(JSON_SET_NAME, set.name)
        }.toString()
    }

    companion object {
        private const val JSON_TOKEN_NAME = "token"
        private const val JSON_URL_NAME = "url"
        private const val JSON_SET_NAME = "set"


        fun fromString(str: String?): EmoteCardModelWrapper? {
            str?.toIntOrNull()?.let {
                return null
            }

            return try {
                if (str.isNullOrEmpty()) {
                    null
                } else {
                    with(JSONObject(str)) {
                        EmoteCardModelWrapper(
                            token = getString(JSON_TOKEN_NAME),
                            url = getString(JSON_URL_NAME),
                            set = EmotePackageSet.valueOf(getString(JSON_SET_NAME))
                        )
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }
}

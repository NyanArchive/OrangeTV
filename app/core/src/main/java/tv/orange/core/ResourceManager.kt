package tv.orange.core

import tv.twitch.android.app.core.ApplicationContext

object ResourceManager {
    fun getString(resName: String?): String {
        resName ?: run {
            return "resName is null"
        }

        val context = ApplicationContext.getInstance().context
        val id = getId(resName, "string")
        return if (id == 0 || id == -1) {
            "$resName: ID NOT FOUND"
        } else {
            context.getString(id)
        }
    }

    private fun getId(resName: String, type: String): Int {
        val context = ApplicationContext.getInstance().context
        return context.resources.getIdentifier(resName, type, context.packageName)
    }
}
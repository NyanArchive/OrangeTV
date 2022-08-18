package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.flag.Internal.*
import tv.orange.models.abc.Feature
import tv.twitch.android.app.core.ThemeManager
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    val context: Context
) : SharedPreferences.OnSharedPreferenceChangeListener, Feature {
    private val twitch = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    private val orange = context.getSharedPreferences(ORANGE_PREFERENCES, Context.MODE_PRIVATE)
    private val listeners = mutableSetOf<FlagListener>()

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == twitch) {
            if (key == TWITCH_DARK_THEME_KEY) {
                isDarkTheme = ThemeManager.Companion!!.isNightModeEnabled(context)
                Logger.debug("isDarkTheme: $isDarkTheme")
            }
            return
        }

        key?.let { value ->
            Flag.findByKey(value)?.let { flag ->
                readSettingFromPref(flag)
                listeners.forEach { it.onFlagChanged(flag) }
            }
        }
    }

    override fun onDestroyFeature() {
        throw IllegalStateException("PreferenceManager cannot be destroyed")
    }

    override fun onCreateFeature() {}

    fun getChommentSeekerValue(id: String): Int {
        return chommentSeekerCache[id] ?: 0
    }

    fun saveChommentSeekerValue(id: String, value: Int) {
        chommentSeekerCache[id] = value
    }

    fun registerFlagListeners(vararg l: FlagListener) {
        l.forEach { listener ->
            listeners.add(listener)
        }
    }

    fun unregisterFlagListeners(vararg l: FlagListener) {
        l.forEach { listener ->
            listeners.remove(listener)
        }
    }

    fun writeInt(flag: Flag, value: Int) {
        orange.edit().putInt(flag.prefKey, value).apply()
    }

    fun writeBoolean(flag: Flag, value: Boolean) {
        orange.edit().putBoolean(flag.prefKey, value).apply()
    }

    fun writeString(flag: Flag, value: String) {
        orange.edit().putString(flag.prefKey, value).apply()
    }

    private fun readBoolean(flag: Flag): BooleanValue {
        val value = orange.getBoolean(flag.prefKey, Flag.getBoolean(flag.default))
        return BooleanValue(value)
    }

    private fun readInt(flag: Flag): IntegerValue {
        val value = orange.getInt(flag.prefKey, Flag.getInt(flag.default))
        return IntegerValue(value)
    }

    private fun readString(flag: Flag): StringValue {
        val value = orange.getString(flag.prefKey, Flag.getString(flag.default))
        return StringValue(value)
    }

    private fun readSettingFromPref(flag: Flag) {
        when (val value = flag.value) {
            is BooleanValue -> flag.value = readBoolean(flag)
            is IntegerValue -> flag.value = readInt(flag)
            is StringValue -> flag.value = readString(flag)
            is ListValue<*> -> value.set(readString(flag).value)
            is IntegerRangeValue -> value.currentValue = readInt(flag).value
            else -> throw IllegalStateException("debug")
        }
    }

    companion object {
        private const val TWITCH_DARK_THEME_KEY = "user_theme"

        private const val ORANGE_PREFERENCES = "orange"

        var isDarkTheme = false

        @JvmStatic
        fun get() = Core.getFeature(PreferenceManager::class.java)

        private val chommentSeekerCache = mutableMapOf<String, Int>()
    }

    fun initialize() {
        Flag.values().forEach { setting ->
            readSettingFromPref(setting)
        }

        orange.registerOnSharedPreferenceChangeListener(this)
        twitch.registerOnSharedPreferenceChangeListener(this)

        isDarkTheme = ThemeManager.Companion!!.isNightModeEnabled(context)
        Logger.debug("isDarkTheme: $isDarkTheme")
    }
}
package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.flag.Internal.*
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Feature
import tv.twitch.android.app.core.ThemeManager
import javax.inject.Inject

@Suppress("DEPRECATION")
@AutoInitialize
class PreferenceManager @Inject constructor(
    val context: Context,
    val themeManager: ThemeManager.Companion
) : SharedPreferences.OnSharedPreferenceChangeListener, Feature {
    private val twitch = getTwitchSharedPreferences(context)
    private val orange = getOrangeSharedPreferences(context)

    private val listeners = mutableSetOf<FlagListener>()

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        when (preferences) {
            twitch -> onTwitchSharedPreferenceChanged(key = key)
            orange -> onOrangeSharedPreferenceChanged(key = key)
            else -> {
                LoggerImpl.warning("Unknown pref: $preferences")
            }
        }
    }

    private fun onTwitchSharedPreferenceChanged(key: String?) {
        isDarkThemeEnabled = themeManager.isNightModeEnabled(context)
    }

    private fun onOrangeSharedPreferenceChanged(key: String?) {
        key?.let { value ->
            Flag.findByKey(prefKey = value)?.let { flag ->
                readFromPreferences(flag = flag)
                listeners.forEach { it.onFlagValueChanged(flag = flag) }
                pCases(flag)
            }
        }
    }

    private fun pCases(flag: Flag) {
        if (flag == Flag.DEV_MODE) {
            LoggerImpl.devMode = flag.asBoolean()
        }
    }

    fun getChommentSeekerValue(videoId: String): Int {
        return chommentSeekerCache[videoId] ?: 0
    }

    fun saveChommentSeekerValue(videoId: String, value: Int) {
        chommentSeekerCache[videoId] = value
    }

    fun registerFlagListeners(vararg l: FlagListener) {
        l.forEach { listener ->
            listeners.add(listener)
        }
    }

    fun writeInt(flag: Flag, value: Int) {
        orange.edit().putInt(flag.preferenceKey, value).apply()
    }

    fun writeBoolean(flag: Flag, value: Boolean) {
        orange.edit().putBoolean(flag.preferenceKey, value).apply()
    }

    fun writeString(flag: Flag, value: String) {
        orange.edit().putString(flag.preferenceKey, value).apply()
    }

    private fun readBoolean(flag: Flag): BooleanValue {
        val value = orange.getBoolean(
            flag.preferenceKey,
            Flag.getBoolean(holder = flag.defaultHolder)
        )
        return BooleanValue(value)
    }

    private fun readInt(flag: Flag): IntegerValue {
        val value = orange.getInt(
            flag.preferenceKey,
            Flag.getInt(holder = flag.defaultHolder)
        )
        return IntegerValue(value)
    }

    private fun readString(flag: Flag): StringValue {
        val value = orange.getString(
            flag.preferenceKey,
            Flag.getString(holder = flag.defaultHolder)
        )
        return StringValue(value)
    }

    private fun readFromPreferences(flag: Flag) {
        when (val value = flag.valueHolder) {
            is BooleanValue -> flag.valueHolder = readBoolean(flag = flag)
            is IntegerValue -> flag.valueHolder = readInt(flag = flag)
            is StringValue -> flag.valueHolder = readString(flag = flag)
            is ListValue<*> -> value.setCurrentVariant(value = readString(flag = flag).value)
            is IntegerRangeValue -> value.setCurrentValue(value = readInt(flag = flag).value)
            else -> throw IllegalStateException("WTF")
        }
    }

    companion object {
        private const val ORANGE_SHARED_PREFERENCES_NAME = "orange"

        private const val ORANGE_TIMER_HOURS_KEY = "orange_timer_hours"
        private const val ORANGE_TIMER_MINUTES_KEY = "orange_timer_minutes"

        var isDarkThemeEnabled = false

        @JvmStatic
        fun get() = Core.getFeature(PreferenceManager::class.java)

        private val chommentSeekerCache = mutableMapOf<String, Int>()

        @JvmStatic
        fun getOrangeSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                ORANGE_SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
        }

        @JvmStatic
        fun getTwitchSharedPreferences(context: Context): SharedPreferences {
            return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    fun initialize() {
        Flag.values().forEach { setting ->
            readFromPreferences(setting)
        }

        orange.registerOnSharedPreferenceChangeListener(this)
        twitch.registerOnSharedPreferenceChangeListener(this)

        isDarkThemeEnabled = ThemeManager.Companion!!.isNightModeEnabled(context)
        LoggerImpl.devMode = Flag.DEV_MODE.asBoolean()
    }

    fun getLastTimer(): Pair<Int, Int> {
        return orange.getInt(
            ORANGE_TIMER_HOURS_KEY,
            0
        ) to orange.getInt(
            ORANGE_TIMER_MINUTES_KEY,
            1
        )
    }

    fun saveLastTimer(data: Pair<Int, Int>) {
        orange.edit().putInt(ORANGE_TIMER_HOURS_KEY, data.first).apply()
        orange.edit().putInt(ORANGE_TIMER_MINUTES_KEY, data.second).apply()
    }

    override fun onCreateFeature() {
        initialize()
    }
}
package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asString
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.flag.core.*
import tv.twitch.android.app.core.ApplicationContext
import tv.twitch.android.app.core.ThemeManager

@Suppress("DEPRECATION")
object PreferencesManagerCore : SharedPreferences.OnSharedPreferenceChangeListener {
    private const val ORANGE_SHARED_PREFERENCES_NAME = "orange"

    private const val ORANGE_TIMER_HOURS_KEY = "orange_timer_hours"
    private const val ORANGE_TIMER_MINUTES_KEY = "orange_timer_minutes"

    private val twitch = getTwitchSharedPreferences(context)
    private val orange = getOrangeSharedPreferences(context)

    private val listeners = mutableSetOf<FlagListener>()

    var isDarkThemeEnabled = false
    var isInitialized = false

    private val chommentSeekerCache = mutableMapOf<String, Int>()

    private val context: Context
        get() = ApplicationContext.getInstance().getContext()

    override fun onSharedPreferenceChanged(pref: SharedPreferences?, key: String?) {
        when (pref) {
            twitch -> onTwitchSharedPreferenceChanged(key = key)
            orange -> onOrangeSharedPreferenceChanged(key = key)
            else -> {
                LoggerImpl.warning("Unknown pref: $pref")
            }
        }
    }

    private fun onTwitchSharedPreferenceChanged(key: String?) {
        isDarkThemeEnabled = ThemeManager.Companion!!.isNightModeEnabled(context)
    }

    private fun onOrangeSharedPreferenceChanged(key: String?) {
        key?.let { value ->
            Flag.findByKey(prefKey = value)?.let { flag ->
                setFromPreferences(flag = flag)
                listeners.forEach { it.onFlagValueChanged(flag = flag) }
                LoggerImpl.devMode = flag.asBoolean()
            }
        }
    }

    private fun writeInt(flag: Flag, value: Int) {
        orange.edit().putInt(flag.preferenceKey, value).apply()
    }

    private fun writeBoolean(flag: Flag, value: Boolean) {
        orange.edit().putBoolean(flag.preferenceKey, value).apply()
    }

    private fun writeString(flag: Flag, value: String) {
        orange.edit().putString(flag.preferenceKey, value).apply()
    }

    private fun readRawString(flag: Flag): String? {
        return orange.getString(
            flag.preferenceKey,
            Flag.getString(holder = flag.defaultHolder)
        )
    }

    private fun readRawInt(flag: Flag): Int {
        return orange.getInt(
            flag.preferenceKey,
            Flag.getInt(holder = flag.defaultHolder)
        )
    }

    private fun readRawBoolean(flag: Flag): Boolean {
        return orange.getBoolean(
            flag.preferenceKey,
            Flag.getBoolean(holder = flag.defaultHolder)
        )
    }

    private fun setFromPreferences(flag: Flag) {
        when (val value = flag.valueHolder) {
            is BooleanValue -> flag.valueHolder.setValue(readRawBoolean(flag = flag))
            is IntValue -> flag.valueHolder.setValue(readRawInt(flag = flag))
            is StringValue -> flag.valueHolder.setValue(readRawString(flag = flag))
            is ListValue<*> -> flag.valueHolder.setValue(readRawString(flag = flag))
            is IntRangeValue -> flag.valueHolder.setValue(readRawInt(flag = flag))
            else -> throw IllegalStateException("Illegal subclass: ${value.javaClass}")
        }
    }

    private fun getOrangeSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            ORANGE_SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }

    private fun getTwitchSharedPreferences(context: Context): SharedPreferences {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getLastTimerData(): Pair<Int, Int> {
        return orange.getInt(
            ORANGE_TIMER_HOURS_KEY,
            0
        ) to orange.getInt(
            ORANGE_TIMER_MINUTES_KEY,
            1
        )
    }

    fun saveLastTimerData(data: Pair<Int, Int>) {
        orange.edit().putInt(ORANGE_TIMER_HOURS_KEY, data.first).apply()
        orange.edit().putInt(ORANGE_TIMER_MINUTES_KEY, data.second).apply()
    }

    fun setUserMentionColor(newColor: Int) {
        writeInt(Flag.USER_MENTION_COLOR, newColor)
    }

    fun saveToPreferences(flag: Flag) {
        when (val value = flag.valueHolder) {
            is BooleanValue -> writeBoolean(flag = flag, flag.asBoolean())
            is IntValue -> writeInt(flag = flag, flag.asInt())
            is IntRangeValue -> writeInt(flag = flag, flag.asInt())
            is StringValue -> writeString(flag = flag, flag.asString())
            is ListValue<*> -> writeString(flag = flag, flag.asString())
            else -> throw IllegalStateException("Illegal subclass: ${value.javaClass}")
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

    fun initialize() {
        if (isInitialized) {
            return
        }

        Flag.values().forEach { setting ->
            setFromPreferences(setting)
        }

        orange.registerOnSharedPreferenceChangeListener(this)
        twitch.registerOnSharedPreferenceChangeListener(this)

        isDarkThemeEnabled = ThemeManager.Companion!!.isNightModeEnabled(context)
        LoggerImpl.devMode = Flag.DEV_MODE.asBoolean()
        isInitialized = true
    }
}
package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.models.Flag
import tv.orange.core.models.FlagListener
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val preferences = context.getSharedPreferences(ORANGE_PREFERENCES, Context.MODE_PRIVATE)

    private val listeners = mutableSetOf<FlagListener>()

    fun register(listener: FlagListener) {
        listeners.add(listener)
    }

    fun unregister(listener: FlagListener) {
        listeners.remove(listener)
    }

    fun initialize() {
        Flag.values().forEach { setting ->
            readSettingFromPref(setting)
        }

        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    fun writeBoolean(flag: Flag, value: Boolean) {
        Logger.debug("setting: $flag, value: $value")
        preferences.edit().putBoolean(flag.prefKey, value).apply()
    }

    fun writeBoolean(prefKey: String, value: Boolean) {
        Logger.debug("prefKey: $prefKey, value: $value")

        Flag.findByKey(prefKey)?.let { flag ->
            writeBoolean(flag, value)
        } ?: throw IllegalStateException(prefKey)
    }

    private fun readBoolean(flag: Flag): Flag.BooleanValue {
        Logger.debug("flag: $flag")
        val value = preferences.getBoolean(flag.prefKey, Flag.getBoolean(flag.default))
        return Flag.BooleanValue(value)
    }

    private fun readInt(flag: Flag): Flag.IntegerValue {
        Logger.debug("flag: $flag")
        val value = preferences.getInt(flag.prefKey, Flag.getInt(flag.default))
        return Flag.IntegerValue(value)
    }

    private fun readString(flag: Flag): Flag.StringValue {
        Logger.debug("flag: $flag")
        val value = preferences.getString(flag.prefKey, Flag.getString(flag.default))
        return Flag.StringValue(value)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let { value ->
            Flag.findByKey(value)?.let { flag ->
                readSettingFromPref(flag)
                listeners.forEach { it.onFlagChanged(flag) }
            }
        }
    }

    private fun readSettingFromPref(flag: Flag) {
        when (flag.value) {
            is Flag.BooleanValue -> flag.value = readBoolean(flag)
            is Flag.IntegerValue -> flag.value = readInt(flag)
            is Flag.StringValue -> flag.value = readString(flag)
            else -> throw IllegalStateException("debug")
        }
    }

    companion object {
        private const val ORANGE_PREFERENCES = "orange"

        private val INSTANCE by lazy {
            val instance =
                Core.getInjector().provideComponent(CoreComponent::class).preferenceManager

            Logger.debug("Provide new instance: $instance")
            return@lazy instance
        }

        @JvmStatic
        fun get(): PreferenceManager {
            return INSTANCE
        }
    }
}
package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.models.Flag
import tv.orange.core.models.FlagListener
import tv.orange.models.abc.Feature
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    context: Context
) : SharedPreferences.OnSharedPreferenceChangeListener, Feature {
    private val preferences = context.getSharedPreferences(ORANGE_PREFERENCES, Context.MODE_PRIVATE)
    private val listeners = mutableSetOf<FlagListener>()

    fun getChommentSeekerValue(id: String): Int {
        return localChommentsSeekerOpt[id] ?: 0
    }

    fun saveChommentSeekerValue(id: String, value: Int) {
        localChommentsSeekerOpt[id] = value
    }

    fun registerFlagListeners(vararg l: FlagListener) {
        l.forEach { listener ->
            Logger.debug("register: $l")
            listeners.add(listener)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key?.let { value ->
            Flag.findByKey(value)?.let { flag ->
                readSettingFromPref(flag)
                listeners.forEach { it.onFlagChanged(flag) }
            }
        }
    }

    fun unregisterFlagListeners(vararg l: FlagListener) {
        l.forEach { listener ->
            Logger.debug("unregister: $l")
            listeners.remove(listener)
        }
    }

    fun writeBoolean(flag: Flag, value: Boolean) {
        preferences.edit().putBoolean(flag.prefKey, value).apply()
    }

    fun writeString(flag: Flag, value: String) {
        preferences.edit().putString(flag.prefKey, value).apply()
    }

    fun writeBoolean(prefKey: String, value: Boolean) {
        Flag.findByKey(prefKey)?.let { flag ->
            writeBoolean(flag, value)
        } ?: throw IllegalStateException(prefKey)
    }

    private fun readBoolean(flag: Flag): Flag.BooleanValue {
        val value = preferences.getBoolean(flag.prefKey, Flag.getBoolean(flag.default))
        return Flag.BooleanValue(value)
    }

    private fun readInt(flag: Flag): Flag.IntegerValue {
        val value = preferences.getInt(flag.prefKey, Flag.getInt(flag.default))
        return Flag.IntegerValue(value)
    }

    private fun readString(flag: Flag): Flag.StringValue {
        val value = preferences.getString(flag.prefKey, Flag.getString(flag.default))
        return Flag.StringValue(value)
    }

    private fun readRawString(flag: Flag): String {
        return preferences.getString(flag.prefKey, Flag.getString(flag.default))!!
    }

    private fun readSettingFromPref(flag: Flag) {
        when (flag.value) {
            is Flag.BooleanValue -> flag.value = readBoolean(flag)
            is Flag.IntegerValue -> flag.value = readInt(flag)
            is Flag.StringValue -> flag.value = readString(flag)
            is Flag.ListValue<*> -> (flag.value as Flag.ListValue<*>).set(readRawString(flag))
            else -> throw IllegalStateException("debug")
        }
    }

    companion object {
        private const val ORANGE_PREFERENCES = "orange"

        private val localChommentsSeekerOpt = mutableMapOf<String, Int>()

        @JvmStatic
        fun get() = Core.getFeature(PreferenceManager::class.java)
    }

    fun initialize() {
        Flag.values().forEach { setting ->
            readSettingFromPref(setting)
        }

        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyFeature() {
        throw IllegalStateException("PreferenceManager cannot be destroyed")
    }
    override fun onCreateFeature() {}
}
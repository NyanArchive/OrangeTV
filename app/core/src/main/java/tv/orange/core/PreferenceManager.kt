package tv.orange.core

import android.content.Context
import android.content.SharedPreferences
import tv.orange.core.di.component.CoreComponent
import javax.inject.Inject

class PreferenceManager @Inject constructor(context: Context) :
    SharedPreferences.OnSharedPreferenceChangeListener {
    private val preferences = context.getSharedPreferences(ORANGE_PREFERENCES, Context.MODE_PRIVATE)

    fun initialize() {
        devMode = readBoolean("devMode")
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    fun toggleDevMode(state: Boolean) {
        Logger.debug("state: $state")
        writeBoolean("devMode", state)
    }

    private fun writeBoolean(key: String, value: Boolean) {
        Logger.debug("key: $key, value: $value")
        preferences.edit().putBoolean(key, value).apply()
    }

    private fun readBoolean(name: String, defaultState: Boolean = false): Boolean {
        Logger.debug("name: $name, defaultState: $defaultState")
        return preferences.getBoolean(name, defaultState)
    }

    companion object {
        private const val ORANGE_PREFERENCES = "orange"

        var devMode = false

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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Logger.debug("key: $key")
        when (key) {
            "devMode" -> devMode = readBoolean(key)
            else -> Logger.debug("Check: $key")
        }
    }
}
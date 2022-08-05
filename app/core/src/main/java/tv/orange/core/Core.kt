package tv.orange.core

import android.content.Context
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.models.abc.Bridge
import tv.orange.models.abc.Feature
import javax.inject.Inject
import kotlin.system.exitProcess

class Core @Inject constructor(val context: Context) :
    LifecycleController,
    LifecycleAware,
    Feature {
    private val modules = mutableSetOf<LifecycleAware>()

    companion object {
        private lateinit var bridge: Bridge

        @JvmStatic
        fun get() = getFeature(Core::class.java)

        @JvmStatic
        fun setBridge(bridge: Bridge) {
            this.bridge = bridge
        }

        @JvmStatic
        fun <T : Feature> getFeature(clazz: Class<T>): T {
            Logger.debug("request: $clazz")
            return getBridge().getFeature(clazz)
        }

        @JvmStatic
        private fun getBridge(): Bridge {
            return bridge
        }

        @JvmStatic
        fun killApp() {
            exitProcess(0);
        }
    }

    override fun registerLifecycleListeners(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            modules.add(listener)
        }
    }

    override fun unregisterLifecycleListener(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            modules.remove(listener)
        }
    }

    override fun onAllComponentDestroyed() {
        Logger.debug("onAllComponentDestroyed")
        modules.forEach {
            it.onAllComponentDestroyed()
        }
    }

    override fun onAllComponentStopped() {
        Logger.debug("onAllComponentStopped")
        modules.forEach {
            it.onAllComponentStopped()
        }
    }

    override fun onSdkResume() {
        Logger.debug("onSdkResume")
        modules.forEach {
            it.onSdkResume()
        }
    }

    override fun onAccountLogout() {
        Logger.debug("onAccountLogout")
        modules.forEach {
            it.onAccountLogout()
        }
    }

    override fun onFirstActivityCreated() {
        Logger.debug("onFirstActivityCreated")
        modules.forEach {
            it.onFirstActivityCreated()
        }
    }

    override fun onFirstActivityStarted() {
        Logger.debug("onFirstActivityStarted")
        modules.forEach {
            it.onFirstActivityStarted()
        }
    }

    override fun onConnectedToChannel(channelId: Int) {
        Logger.debug("onConnectedToChannel")
        modules.forEach {
            it.onConnectedToChannel(channelId)
        }
    }

    override fun onConnectingToChannel(channelId: Int) {
        Logger.debug("onConnectingToChannel")
        modules.forEach {
            it.onConnectingToChannel(channelId)
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
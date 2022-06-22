package tv.orange.core

import tv.orange.core.di.CoreComponent
import tv.orange.core.di.DaggerCoreComponent
import tv.orange.core.models.LifecycleController
import tv.orange.core.models.LifecycleAware

class Core private constructor(val coreComponent: CoreComponent) : LifecycleController, LifecycleAware {
    val modules: HashSet<LifecycleAware> = HashSet()

    companion object {
        lateinit var INSTANCE: Core

        @JvmStatic
        fun get(): Core {
            return INSTANCE
        }

        fun initialize() {
            if (::INSTANCE.isInitialized) {
                throw IllegalStateException()
            }
            val core = Core(DaggerCoreComponent.factory().create())
            Logger.debug("created: $core")
            INSTANCE = core
        }
    }

    override fun registerLifecycleListener(lifecycleAware: LifecycleAware) {
        Logger.debug("register: $lifecycleAware")
        modules.add(lifecycleAware)
    }

    override fun unregisterLifecycleListener(lifecycleAware: LifecycleAware) {
        Logger.debug("unregister: $lifecycleAware")
        modules.remove(lifecycleAware)
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
}
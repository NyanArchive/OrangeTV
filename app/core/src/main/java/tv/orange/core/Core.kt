package tv.orange.core

import android.content.Context
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.models.Injector
import tv.orange.models.InjectorProvider

class Core private constructor(private val applicationContext: Context) :
    LifecycleController,
    LifecycleAware {
    val modules: HashSet<LifecycleAware> = HashSet()

    companion object {
        private var INSTANCE: Core? = null

        fun initialize(applicationContext: Context) {
            INSTANCE = Core(applicationContext)
            PreferenceManager.get().initialize()
        }

        @JvmStatic
        fun get(): Core {
            return INSTANCE ?: kotlin.run {
                throw IllegalStateException("Instance is null")
            }
        }

        fun getInjector(): Injector {
            val context = get().applicationContext
            if (context is InjectorProvider) {
                return context.provideInjector()
            } else {
                throw IllegalStateException("App context must provide Injector")
            }
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

    override fun onConnectingToChannel(channelId: Int) {
        Logger.debug("onConnectingToChannel")
        modules.forEach {
            it.onConnectingToChannel(channelId)
        }
    }
}
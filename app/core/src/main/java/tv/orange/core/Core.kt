package tv.orange.core

import android.content.Context
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.models.Injector
import tv.orange.models.InjectorProvider
import javax.inject.Provider
import kotlin.reflect.KClass

class Core private constructor(private val applicationContext: Context) :
    LifecycleController,
    LifecycleAware,
    Injector {
    private val modules = mutableSetOf<LifecycleAware>()

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

        private fun getInjector(): Injector {
            val context = get().applicationContext
            if (context is InjectorProvider) {
                return context.provideInjector()
            } else {
                throw IllegalStateException("App context must provide Injector")
            }
        }
    }

    override fun registerLifecycleListeners(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            Logger.debug("register: $listener")
            modules.add(listener)
        }
    }

    override fun unregisterLifecycleListener(vararg listeners: LifecycleAware) {
        listeners.forEach { listener ->
            Logger.debug("unregister: $listener")
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

    override fun <T : Any> provideComponent(cls: KClass<T>): Provider<T> {
        return getInjector().provideComponent(cls)
    }
}
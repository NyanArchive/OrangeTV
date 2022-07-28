package tv.orange.core

import android.content.Context
import android.os.Build
import androidx.fragment.app.FragmentActivity
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

        @JvmStatic
        fun <T : Any> getProvider(componentClass: KClass<T>): Provider<T> {
            return get().getComponentProvider(componentClass)
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

    override fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T> {
        val injector = if (applicationContext is InjectorProvider) {
            applicationContext.provideInjector()
        } else {
            throw IllegalStateException("App context must provide Injector")
        }

        return injector.getComponentProvider(cls)
    }

    fun killApp() {
        System.exit(0);
    }
}
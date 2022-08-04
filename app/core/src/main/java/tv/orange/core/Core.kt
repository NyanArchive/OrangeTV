package tv.orange.core

import android.content.Context
import tv.orange.core.models.LifecycleAware
import tv.orange.core.models.LifecycleController
import tv.orange.models.*
import tv.twitch.android.app.core.ApplicationContext
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass
import kotlin.system.exitProcess

class Core @Inject constructor(val context: Context) :
    LifecycleController,
    LifecycleAware,
    Injector,
    Feature {
    private val modules = mutableSetOf<LifecycleAware>()

    companion object {
        @JvmStatic
        fun <T : Feature> getFeature(clazz: Class<T>): T {
            Logger.debug("request: $clazz")
            return getBridge().getFeature(clazz)
        }

        @JvmStatic
        fun getBridge(): Bridge {
            val context = ApplicationContext.getInstance().getContext()
            if (context is BridgeProvider) {
                return context.provideBridge()
            }

            throw IllegalStateException("context must provide bridge")
        }

        @JvmStatic
        fun <T : Any> getProvider(componentClass: KClass<T>): Provider<T> {
            val context = ApplicationContext.getInstance().getContext()
            if (context is InjectorProvider) {
                return context.provideInjector().getComponentProvider(componentClass)
            }

            throw IllegalStateException("context must provide injector")
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

    override fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T> {
        val injector = if (context is InjectorProvider) {
            context.provideInjector()
        } else {
            throw IllegalStateException("App context must provide Injector")
        }

        return injector.getComponentProvider(cls)
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
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
        modules.add(lifecycleAware)
    }

    override fun unregisterLifecycleListener(lifecycleAware: LifecycleAware) {
        modules.remove(lifecycleAware)
    }

    override fun onAllComponentDestroyed() {
        modules.forEach {
            it.onAllComponentDestroyed()
        }
    }

    override fun onAllComponentStopped() {
        modules.forEach {
            it.onAllComponentStopped()
        }
    }

    override fun onSdkResume() {
        modules.forEach {
            it.onSdkResume()
        }
    }

    override fun onAccountLogout() {
        modules.forEach {
            it.onAccountLogout()
        }
    }

    override fun onFirstActivityCreated() {
        modules.forEach {
            it.onFirstActivityCreated()
        }
    }

    override fun onFirstActivityStarted() {
        modules.forEach {
            it.onFirstActivityStarted()
        }
    }
}
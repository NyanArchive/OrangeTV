package tv.orange.core.models.lifecycle

interface LifecycleController {
    fun registerLifecycleListeners(vararg listeners: LifecycleAware)

    fun unregisterLifecycleListener(vararg listeners: LifecycleAware)
}
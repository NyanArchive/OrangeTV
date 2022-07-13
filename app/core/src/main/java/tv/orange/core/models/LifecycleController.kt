package tv.orange.core.models

interface LifecycleController {
    fun registerLifecycleListeners(vararg listeners: LifecycleAware)

    fun unregisterLifecycleListener(vararg listeners: LifecycleAware)
}
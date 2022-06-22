package tv.orange.core.models

interface LifecycleController {
    fun registerLifecycleListener(lifecycleAware: LifecycleAware)

    fun unregisterLifecycleListener(lifecycleAware: LifecycleAware)
}
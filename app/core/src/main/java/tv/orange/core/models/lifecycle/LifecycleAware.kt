package tv.orange.core.models.lifecycle

interface LifecycleAware {
    fun onAllComponentDestroyed()

    fun onAllComponentStopped()

    fun onSdkResume()

    fun onAccountLogout()

    fun onFirstActivityCreated()

    fun onFirstActivityStarted()

    fun onConnectedToChannel(channelId: Int)

    fun onConnectingToChannel(channelId: Int)
}
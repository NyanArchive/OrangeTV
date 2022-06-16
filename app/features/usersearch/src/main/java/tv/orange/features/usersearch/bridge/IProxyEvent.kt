package tv.orange.features.usersearch.bridge

import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent

interface IProxyEvent {
    fun proxyEvent(event: ViewDelegateEvent)
}
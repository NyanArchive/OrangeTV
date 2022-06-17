package tv.orange.features.usersearch

import androidx.appcompat.widget.SearchView
import tv.orange.core.compat.ClassCompat.cast
import tv.orange.features.usersearch.bridge.IProxyEvent
import tv.orange.features.usersearch.bridge.IViewerListViewDelegate
import tv.orange.features.usersearch.view.ViewFactory
import tv.orange.features.usersearch.view.ViewFactoryImpl
import tv.twitch.android.core.mvp.rxutil.DisposeOn
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent
import tv.twitch.android.models.chat.Chatters
import tv.twitch.android.shared.chat.viewerlist.ViewerListPresenter
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate

class Hook(private val viewFactory: ViewFactory) {
    fun getSearchBar(delegate: ViewerListViewDelegate): SearchView? {
        return viewFactory.createSearchBar(delegate)?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    (delegate.cast<IViewerListViewDelegate>()).onInputSearchTextChanged(newText)

                    return false
                }
            })
        }
    }

    fun filterChatters(chatters: Chatters?, searchUserText: String?): Chatters? {
        chatters ?: return null
        searchUserText ?: return chatters

        if (searchUserText.isNullOrBlank()) {
            return chatters
        }

        val broadcasters = filterUserList(chatters.broadcasters.cast(), searchUserText)
        val staff = filterUserList(chatters.staff.cast(), searchUserText)
        val mods = filterUserList(chatters.moderators.cast(), searchUserText)
        val vips = filterUserList(chatters.vips.cast(), searchUserText)
        val viewers = filterUserList(chatters.viewers.cast(), searchUserText)

        return Chatters(chatters.numViewers, broadcasters, staff, mods, vips, viewers)
    }

    private fun filterUserList(users: List<String>, part: String): List<String> {
        if (users.isEmpty()) {
            return users
        }

        if (part.isBlank()) {
            return users
        }

        return users.filter { it.contains(part, ignoreCase = true) }
    }

    fun setupFilter(
        viewDelegate: ViewerListViewDelegate,
        presenter: ViewerListPresenter
    ) {
        presenter.directSubscribe(viewDelegate.eventObserver(), DisposeOn.DESTROY) { event ->
            if (event is ViewDelegateEvent) {
                (presenter.cast<IProxyEvent>()).proxyEvent(event)
            }
        }
    }

    companion object {
        val instance by lazy {
            Hook(ViewFactoryImpl())
        }
    }
}
package tv.orange.features.usersearch

import androidx.appcompat.widget.SearchView
import tv.orange.features.usersearch.bridge.IProxyEvent
import tv.orange.features.usersearch.bridge.IViewerListViewDelegate
import tv.twitch.android.core.mvp.rxutil.DisposeOn
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent
import tv.twitch.android.models.chat.Chatters
import tv.twitch.android.shared.chat.viewerlist.ViewerListPresenter
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate

object Hook {
    private val viewFactory = ViewFactoryImpl()

    fun getSearchBar(delegate: ViewerListViewDelegate): SearchView? {
        return viewFactory.createSearchBar(delegate)?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val ref = delegate as Any
                    if (ref is IViewerListViewDelegate) {
                        ref.onInputSearchTextChanged(newText)
                    }

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

        val broadcasters = filterUserList(chatters.broadcasters as List<String>, searchUserText)
        val staff = filterUserList(chatters.staff as List<String>, searchUserText)
        val mods = filterUserList(chatters.moderators as List<String>, searchUserText)
        val vips = filterUserList(chatters.vips as List<String>, searchUserText)
        val viewers = filterUserList(chatters.viewers as List<String>, searchUserText)

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
            val proxyPresenter = presenter as Any
            if (proxyPresenter is IProxyEvent) {
                if (event is ViewDelegateEvent) {
                    proxyPresenter.proxyEvent(event)
                }
            }
        }
    }
}
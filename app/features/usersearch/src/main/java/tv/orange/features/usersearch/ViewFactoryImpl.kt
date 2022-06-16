package tv.orange.features.usersearch

import androidx.appcompat.widget.SearchView
import tv.orange.core.Logger
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate

class ViewFactoryImpl : ViewFactory {
    override fun createSearchBar(delegate: ViewerListViewDelegate): SearchView? {
        val id = delegate.context.resources.getIdentifier(
            "viewer_list_dialog__search",
            "id",
            delegate.context.packageName
        )
        Logger.debug("id:$id")
        if (id == 0) {
            return null
        }

        val view = delegate.contentView.findViewById<SearchView>(id)
        Logger.debug("view:$view")

        return view
    }
}
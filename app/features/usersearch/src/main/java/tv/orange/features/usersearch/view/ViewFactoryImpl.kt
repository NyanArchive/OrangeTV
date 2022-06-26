package tv.orange.features.usersearch.view

import androidx.appcompat.widget.SearchView
import tv.orange.features.usersearch.di.scope.UserSearchScope
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate
import javax.inject.Inject

@UserSearchScope
class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createSearchBar(delegate: ViewerListViewDelegate): SearchView? {
        val id = delegate.context.resources.getIdentifier(
            "viewer_list_dialog__search",
            "id",
            delegate.context.packageName
        )
        if (id == 0) {
            return null
        }

        return delegate.contentView.findViewById(id)
    }
}
package tv.orange.features.usersearch.view

import androidx.appcompat.widget.SearchView
import tv.orange.core.util.ViewUtil.getView
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate
import javax.inject.Inject

class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun createSearchBar(delegate: ViewerListViewDelegate): SearchView {
        return delegate.contentView.getView(resName = "viewer_list_dialog__search")
    }
}
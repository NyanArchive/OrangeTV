package tv.orange.features.usersearch.view

import androidx.appcompat.widget.SearchView
import tv.twitch.android.shared.chat.viewerlist.ViewerListViewDelegate

interface ViewFactory {
    fun createSearchBar(delegate: ViewerListViewDelegate): SearchView?
}
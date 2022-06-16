package tv.orange.features.usersearch.bridge

import androidx.appcompat.widget.SearchView

interface IViewerListViewDelegate {
    fun onInputSearchTextChanged(text: String?)
}
package tv.orange.features.blacklist.mvp

import tv.orange.features.blacklist.data.model.KeywordData

interface BlacklistContract {
    interface View {
        fun render(state: State)
        fun showAddKeywordsDialog()

        sealed class State {
            object Loading : State()
            data class Update(val keywords: List<KeywordData>) : State()
        }
    }

    abstract class Presenter(val view: View) {
        abstract fun onStart()
        abstract fun onStop()

        abstract fun onAddButtonClicked()

        abstract fun onChangeTypeItemClicked(keyword: KeywordData)

        abstract fun onItemRemoved(keyword: KeywordData)

        abstract fun addNewItems(rawText: String)
    }
}
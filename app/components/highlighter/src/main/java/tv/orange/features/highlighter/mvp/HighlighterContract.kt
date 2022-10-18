package tv.orange.features.highlighter.mvp

import tv.orange.features.highlighter.data.model.KeywordData

interface HighlighterContract {
    interface View {
        fun render(state: State)
        fun showAddKeywordsDialog()
        fun showChangeItemColorDialog(keyword: KeywordData)

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
        abstract fun onChangeColorItemClicked(keyword: KeywordData)
        abstract fun onVibrationItemClicked(keyword: KeywordData)

        abstract fun onItemRemoved(keyword: KeywordData)
        abstract fun onItemColorChanged(keyword: KeywordData, newColor: Int)

        abstract fun addNewItems(rawText: String)
    }
}
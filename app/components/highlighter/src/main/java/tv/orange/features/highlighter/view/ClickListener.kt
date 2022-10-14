package tv.orange.features.highlighter.view

import tv.orange.features.highlighter.data.model.KeywordData

interface ClickListener {
    fun onChangeColorClicked(item: KeywordData)
}
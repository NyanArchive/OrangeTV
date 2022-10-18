package tv.orange.features.highlighter.view

import androidx.recyclerview.widget.DiffUtil
import tv.orange.features.highlighter.data.model.KeywordData

class HighlighterDiffUtilCallback(
    private val oldList: List<KeywordData>,
    private val newList: List<KeywordData>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old == new
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }
}
package tv.orange.features.blacklist.view

import androidx.recyclerview.widget.DiffUtil
import tv.orange.features.blacklist.data.model.KeywordData

class BlacklistDiffUtilCallback(
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
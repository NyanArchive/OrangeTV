package tv.orange.features.blacklist.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.blacklist.data.model.KeywordData

class BlacklistAdapter: RecyclerView.Adapter<KeywordViewHolder>() {
    private val items = mutableListOf<KeywordData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = parent.inflate("orangetv_highlighter_item")
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(keyword: KeywordData) {
        items.add(keyword)
        notifyDataSetChanged()
    }

    fun removeAt(adapterPosition: Int) {
        items.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    fun setData(keywords: List<KeywordData>) {
        val callback = BlacklistDiffUtilCallback(items, keywords)
        val result = DiffUtil.calculateDiff(callback)
        items.clear()
        items.addAll(keywords)
        result.dispatchUpdatesTo(this)
    }

    fun getItem(pos: Int): KeywordData? {
        return if (pos < 0 || pos >= items.size) {
            null
        } else {
            return items[pos]
        }
    }
}
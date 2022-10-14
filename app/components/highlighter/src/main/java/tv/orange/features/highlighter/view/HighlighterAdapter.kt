package tv.orange.features.highlighter.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.highlighter.data.model.KeywordData

class HighlighterAdapter(
    private val listener: ClickListener
) : RecyclerView.Adapter<KeywordViewHolder>() {
    private val items = mutableListOf<KeywordData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        val view = parent.inflate("orangetv_highlighter_item")
        return KeywordViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(items[position], listener)
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

    fun addAll(vararg keywords: KeywordData) {
        items.addAll(keywords)
        notifyDataSetChanged()
    }

    fun setData(keywords: List<KeywordData>) {
        items.clear()
        items.addAll(keywords)
        notifyDataSetChanged()
    }

    fun getItem(pos: Int): KeywordData? {
        return if (pos < 0 || pos >= items.size) {
            null
        } else {
            return items[pos]
        }
    }
}
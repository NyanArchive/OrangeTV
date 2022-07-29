package tv.orange.features.chapters.data.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.orange.core.ResourceManager
import tv.orange.features.chapters.component.data.model.Chapter

class ChaptersAdapter : RecyclerView.Adapter<ChaptersAdapter.ChapterItemVH>() {
    private val chapters: MutableList<Chapter> = mutableListOf()

    fun addChapter(chapter: Chapter) {
        chapters.add(chapter)
        notifyDataSetChanged() // TODO: __DEBUG
    }

    class ChapterItemVH(view: View) : RecyclerView.ViewHolder(view) {
        private val logo = view.findViewById<ImageView>(
            ResourceManager.getId(
                "orange_chapters_item__logo",
                "id"
            )
        )
        private val title = view.findViewById<TextView>(
            ResourceManager.getId(
                "orange_chapters_item__title",
                "id"
            )
        )
        private val timestamp = view.findViewById<TextView>(
            ResourceManager.getId(
                "orange_chapters_item__timestamp",
                "id"
            )
        )

        fun onBind(chapter: Chapter) {
            title.text = chapter.title
            timestamp.text = DateUtils.formatElapsedTime(chapter.timestamp.toLong())
            chapter.url?.let { url ->
                Glide.with(logo).load(url).into(logo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterItemVH {
        val view = LayoutInflater.from(parent.context).inflate(
            ResourceManager.getId("orange_chapters_item", "layout"),
            parent,
            false
        )

        return ChapterItemVH(view)
    }

    override fun onBindViewHolder(holder: ChapterItemVH, position: Int) {
        holder.onBind(chapters[position])
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    fun setData(it: List<Chapter>) {
        chapters.clear()
        chapters.addAll(it)
        notifyDataSetChanged()
    }
}
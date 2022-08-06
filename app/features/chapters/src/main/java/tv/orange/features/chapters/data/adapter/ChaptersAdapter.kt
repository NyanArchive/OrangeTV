package tv.orange.features.chapters.data.adapter

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.chapters.component.data.model.Chapter

class ChaptersAdapter(private val listener: OnChapterClickedListener) :
    RecyclerView.Adapter<ChaptersAdapter.ChapterItemVH>() {
    private var chapters: List<Chapter> = listOf()

    interface OnChapterClickedListener {
        fun onClicked(item: Chapter)
    }

    class ChapterItemVH(view: View) : RecyclerView.ViewHolder(view) {
        private val logo = view.getView<ImageView>("orangetv_chapters_item__logo")
        private val title = view.getView<TextView>("orangetv_chapters_item__title")
        private val timestamp = view.getView<TextView>("orangetv_chapters_item__timestamp")

        fun onBind(listener: OnChapterClickedListener, chapter: Chapter) {
            title.text = chapter.title
            timestamp.text = DateUtils.formatElapsedTime(chapter.timestamp.toLong())
            chapter.url?.let { url ->
                Glide.with(logo).load(url).into(logo)
            }
            itemView.setOnClickListener { listener.onClicked(item = chapter) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterItemVH {
        return ChapterItemVH(parent.inflate("orangetv_chapters_item"))
    }

    override fun onBindViewHolder(holder: ChapterItemVH, position: Int) {
        holder.onBind(listener, chapters[position])
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<Chapter>) {
        chapters = it
        notifyDataSetChanged()
    }
}
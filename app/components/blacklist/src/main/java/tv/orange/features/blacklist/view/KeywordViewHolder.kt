package tv.orange.features.blacklist.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.blacklist.data.model.KeywordData

class KeywordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val keyword = view.getView<TextView>("orangetv_highlighter_item__keyword")
    private val picker = view.getView<ImageView>("orangetv_highlighter_item__picker")
    private val type = view.getView<ImageView>("orangetv_highlighter_item__type")
    private val vibration = view.getView<ImageView>("orangetv_highlighter_item__vibration")

    fun bind(item: KeywordData) {
        keyword.text = item.word
        renderType(item.type)
        picker.changeVisibility(false)
        vibration.changeVisibility(false)
    }

    private fun renderType(keywordType: KeywordData.Type) {
        val resId = ResourceManager.get().getDrawableId(
            resName = when (keywordType) {
                KeywordData.Type.INSENSITIVE -> "ic_orangetv_case_insensitive"
                KeywordData.Type.CASESENSITIVE -> "ic_orangetv_case_sensitive"
                KeywordData.Type.USERNAME -> "ic_orangetv_user"
            }
        )

        type.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                resId
            )
        )
    }
}
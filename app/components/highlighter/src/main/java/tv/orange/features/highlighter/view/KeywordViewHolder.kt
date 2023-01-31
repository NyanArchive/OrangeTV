package tv.orange.features.highlighter.view

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.highlighter.data.model.KeywordData

class KeywordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val keyword = view.getView<TextView>("orangetv_highlighter_item__keyword")
    private val picker = view.getView<ImageView>("orangetv_highlighter_item__picker")
    private val type = view.getView<ImageView>("orangetv_highlighter_item__type")
    private val vibration = view.getView<ImageView>("orangetv_highlighter_item__vibration")

    fun bind(item: KeywordData, listener: ClickListener) {
        keyword.text = item.word
        picker.setOnClickListener {
            listener.onChangeColorClicked(item)
        }
        picker.setImageDrawable(ColorDrawable(item.color))
        renderType(item.type)
        vibration.setOnClickListener {
            listener.onVibrationClicked(item)
        }
        renderVibration(item.vibration)
    }

    private fun renderVibration(vibration: Boolean) {
        val resId = ResourcesManagerCore.get().getDrawableId(
            if (vibration) {
                "ic_orangetv_vibration"
            } else {
                "ic_orangetv_no_vibration"
            }
        )

        this.vibration.setImageDrawable(
            ContextCompat.getDrawable(
                itemView.context,
                resId
            )
        )
    }

    private fun renderType(keywordType: KeywordData.Type) {
        val resId = ResourcesManagerCore.get().getDrawableId(
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
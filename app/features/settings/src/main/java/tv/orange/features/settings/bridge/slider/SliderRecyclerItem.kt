package tv.orange.features.settings.bridge.slider

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asIntRange
import tv.orange.core.util.ViewUtil.getView
import tv.twitch.android.core.adapters.AbstractTwitchRecyclerViewHolder
import tv.twitch.android.core.adapters.ModelRecyclerAdapterItem
import tv.twitch.android.core.adapters.ViewHolderGenerator


class SliderRecyclerItem(
    context: Context,
    model: SliderModel
) : ModelRecyclerAdapterItem<SliderModel>(context, model) {

    override fun bindToViewHolder(vh: RecyclerView.ViewHolder?) {
        if (vh is SliderRecyclerItemViewHolder) {
            vh.title.text = model.primaryText

            vh.slider.valueFrom = model.flag.asIntRange().minValue.toFloat()
            vh.slider.valueTo = model.flag.asIntRange().maxValue.toFloat()
            vh.slider.value = model.flag.asInt().toFloat()
            vh.slider.stepSize = model.flag.asIntRange().step.toFloat()
        }
    }

    override fun getViewHolderResId(): Int {
        return ResourcesManagerCore.get().getLayoutId("orangetv_settings_slider")
    }

    override fun newViewHolderGenerator(): ViewHolderGenerator {
        return ViewHolderGenerator { SliderRecyclerItemViewHolder(it, model.listener) }
    }

    class SliderRecyclerItemViewHolder(
        view: View,
        private val listener: SliderModel.SliderListener
    ) : AbstractTwitchRecyclerViewHolder(view) {
        val title: TextView = view.getView("menu_item_title")
        val slider: Slider = view.getView<Slider>("orangetv_settings_slider__slider").apply {
            addOnChangeListener { _, value, _ ->
                val position = getBindingAdapterPosition()
                if (position != RecyclerView.NO_POSITION) {
                    getBindingDataItem<SliderRecyclerItem>(position)?.let { item ->
                        listener.onSliderValueChanged(item.model.flag, value.toInt())
                    }
                }
            }
        }
    }
}
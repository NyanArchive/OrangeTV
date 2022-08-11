package tv.orange.features.settings.bridge.slider

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import tv.orange.core.ResourceManager
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
            vh.slider.value = model.flag.asIntRange().currentValue.toFloat()
            vh.slider.stepSize = 1F
            vh.slider.addOnChangeListener { _, value, _ ->
                model.listener.onSliderValueChanged(model.flag, value.toInt())
            }
        }
    }

    override fun getViewHolderResId(): Int {
        return ResourceManager.get().getLayoutId("orangetv_settings_slider")
    }

    override fun newViewHolderGenerator(): ViewHolderGenerator {
        return ViewHolderGenerator { SliderRecyclerItemViewHolder(it) }
    }

    class SliderRecyclerItemViewHolder(view: View) : AbstractTwitchRecyclerViewHolder(view) {
        val slider: Slider = view.getView("orangetv_settings_slider__slider")
        val title: TextView = view.getView("menu_item_title")
    }
}
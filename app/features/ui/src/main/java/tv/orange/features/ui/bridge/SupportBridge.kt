package tv.orange.features.ui.bridge

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.BuildConfigUtil
import tv.orange.core.compat.ClassCompat
import tv.twitch.android.core.strings.DateUtil
import java.util.*
import javax.inject.Inject

class SupportBridge @Inject constructor(val context: Context) {
    @SuppressLint("SetTextI18n")
    fun updateLogoutSectionRecyclerItem(viewHolder: RecyclerView.ViewHolder) {
        ClassCompat.invokeIf<ISectionFooterRecyclerItemViewHolder>(viewHolder) {
            BuildConfigUtil.buildConfig.let { config ->
                it.getOrangeTVSection().text = if (config.number > 0) {
                    "PurpleTV/${config.getVersion()}"
                } else {
                    "PurpleTV/Stub"
                }

                it.getOrangeTVDateSection().text = DateUtil.Companion!!.localizedDate(
                    context, if (config.timestamp > 0) {
                        config.timestampToDate()
                    } else {
                        Date(0)
                    }
                )
            }
        }
    }
}
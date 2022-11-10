package tv.orange.features.ui.bridge

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.BuildConfigUtil
import tv.twitch.android.core.strings.DateUtil
import tv.twitch.android.shared.ui.menus.LogoutSectionRecyclerItem
import java.util.*
import javax.inject.Inject

class SupportBridge @Inject constructor(val context: Context) {
    fun updateLogoutSectionRecyclerItem(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is LogoutSectionRecyclerItem.SectionFooterRecyclerItemViewHolder) {
            BuildConfigUtil.buildConfig.let { config ->
                viewHolder.versionTextView.text = if (config.number > 0) {
                    "PurpleTV/${config.number}"
                } else {
                    "PurpleTV/Stub"
                }

                viewHolder.buildDateTextView.text = DateUtil.Companion!!.localizedDate(
                    context,
                    if (config.timestamp > 0) {
                        config.timestampToDate()
                    } else {
                        Date(0)
                    }
                )
            }
        }
    }
}
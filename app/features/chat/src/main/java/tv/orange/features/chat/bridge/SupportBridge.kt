package tv.orange.features.chat.bridge

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tv.twitch.android.core.adapters.RecyclerAdapterItem
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem
import tv.twitch.android.shared.chat.chomments.ChommentRecyclerItem
import tv.twitch.android.shared.chat.messagefactory.adapteritem.PrivateCalloutsMessageRecyclerItem
import tv.twitch.android.shared.chat.messagefactory.adapteritem.RaidMessageRecyclerItem
import tv.twitch.android.shared.chat.messagefactory.adapteritem.SubGoalUserNoticeRecyclerItem
import tv.twitch.android.shared.chat.messagefactory.adapteritem.UserNoticeRecyclerItem
import javax.inject.Inject

class SupportBridge @Inject constructor() {
    interface Callback {
        fun maybeChangeMessageFontSize(textView: TextView)
        fun bindHighlightMessage(vh: RecyclerView.ViewHolder, highlightColor: Int?)
    }

    fun onBindToViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: RecyclerAdapterItem,
        callback: Callback
    ) {
        when (viewHolder) {
            is MessageRecyclerItem.ChatMessageViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.messageTextView)
            }
            is ChommentRecyclerItem.ChommentItemViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.chommentTextView)
            }
            is UserNoticeRecyclerItem.UserNoticeViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.chatMessage)
                callback.maybeChangeMessageFontSize(textView = viewHolder.systemMessage)
            }
            is SubGoalUserNoticeRecyclerItem.SubGoalUserNoticeViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.chatMessage)
                callback.maybeChangeMessageFontSize(textView = viewHolder.goalProgressText)
                callback.maybeChangeMessageFontSize(textView = viewHolder.systemMessage)
            }
            is RaidMessageRecyclerItem.RaidMessageViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.text)
            }
            is PrivateCalloutsMessageRecyclerItem.CalloutMessageViewHolder -> {
                callback.maybeChangeMessageFontSize(textView = viewHolder.body)
            }
        }
        when (item) {
            is IMessageRecyclerItem -> {
                callback.bindHighlightMessage(
                    vh = viewHolder,
                    highlightColor = item.getHighlightColor()
                )
            }
        }
    }
}
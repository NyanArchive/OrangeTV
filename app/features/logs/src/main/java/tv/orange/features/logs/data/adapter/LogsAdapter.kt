package tv.orange.features.logs.data.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.logs.component.data.model.ChatMessage
import tv.orange.features.logs.component.data.model.MessageItem
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import tv.twitch.android.shared.ui.elements.GlideHelper
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class LogsAdapter @Inject constructor(val factoryProvider: ChatMessageFactory.Factory) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messages: List<MessageItem> = listOf()
    lateinit var factory: ChatMessageFactory

    fun bindActivity(fragmentActivity: FragmentActivity) {
        factory = factoryProvider.create(fragmentActivity)
    }

    override fun getItemViewType(position: Int): Int {
        val res = messages[position]

        return if (res is MessageItem.Header) {
            HEADER_TYPE
        } else {
            CONTENT_TYPE
        }
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tv = view.getView<TextView>(resName = "orangetv_logs_date__tv")

        fun onBind(date: Date) {
            tv.text = DateFormat.getDateInstance(DateFormat.SHORT).format(date)
        }
    }

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tv = view.getView<TextView>(resName = "chat_message_item")

        fun onBind(factory: ChatMessageFactory, message: ChatMessage) {
            val spanned = factory.createChatHistoryMessage(
                message.token,
                Color.YELLOW,
                message.channelId
            )
            tv.setText(
                spanned, TextView.BufferType.SPANNABLE
            )
            GlideHelper.loadImagesFromSpanned(itemView.context, spanned, tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> DateViewHolder(parent.inflate(resName = "orangetv_logs_date"))
            else -> MessageViewHolder(parent.inflate(resName = "chat_message_item"))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        if (message is MessageItem.Content) {
            (holder as MessageViewHolder).onBind(factory, message.content)
        } else if (message is MessageItem.Header) {
            (holder as DateViewHolder).onBind(message.date)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<MessageItem>) {
        messages = it
        notifyDataSetChanged()
    }

    companion object {
        private const val CONTENT_TYPE = 0
        private const val HEADER_TYPE = 1
    }
}
package tv.orange.features.logs.data.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.ResourceManager
import tv.orange.core.ViewUtil.getView
import tv.orange.features.logs.component.data.model.Message
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import tv.twitch.android.shared.ui.elements.GlideHelper
import javax.inject.Inject

class LogsAdapter @Inject constructor(val factoryProvider: ChatMessageFactory.Factory) :
    RecyclerView.Adapter<LogsAdapter.ChapterItemVH>() {
    private var messages: List<Message> = listOf()
    lateinit var factory: ChatMessageFactory

    fun bindActivity(fragmentActivity: FragmentActivity) {
        factory = factoryProvider.create(fragmentActivity)
    }

    class ChapterItemVH(view: View) : RecyclerView.ViewHolder(view) {
        private val tv = view.getView<TextView>("chat_message_item")

        fun onBind(factory: ChatMessageFactory, message: Message) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterItemVH {
        val view = LayoutInflater.from(parent.context).inflate(
            ResourceManager.getId("chat_message_item", "layout"),
            parent,
            false
        )

        return ChapterItemVH(view)
    }

    override fun onBindViewHolder(holder: ChapterItemVH, position: Int) {
        holder.onBind(factory, messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<Message>) {
        messages = it
        notifyDataSetChanged()
    }
}
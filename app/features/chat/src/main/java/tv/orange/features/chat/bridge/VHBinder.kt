package tv.orange.features.chat.bridge

import androidx.recyclerview.widget.RecyclerView
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.pronouns.PronounSetter
import tv.twitch.android.core.adapters.RecyclerAdapterItem
import tv.twitch.android.shared.chat.adapter.item.MessageRecyclerItem


object VHBinder {
    private val chp: ChatHookProvider by lazy { ChatHookProvider.get() }

    fun onBindToViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: RecyclerAdapterItem
    ) {
        chp.onBindToViewHolder(viewHolder, item)
    }

    fun bindPronoun(
        holder: MessageRecyclerItem.ChatMessageViewHolder,
        item: RecyclerAdapterItem
    ): PronounSetter? {
        return chp.bindPronoun(holder, item)
    }
}
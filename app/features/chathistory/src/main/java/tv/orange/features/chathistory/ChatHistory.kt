package tv.orange.features.chathistory

import io.reactivex.android.schedulers.AndroidSchedulers
import tv.orange.core.Core
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.chathistory.bridge.ILiveChatSource
import tv.orange.features.chathistory.data.repository.ChatHistoryRepository
import tv.orange.models.abc.Feature
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.shared.chat.events.ChatConnectionEvents
import javax.inject.Inject

class ChatHistory @Inject constructor(
    val repository: ChatHistoryRepository,
    val rm: ResourceManager
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ChatHistory::class.java)
    }

    fun requestChatHistory(
        event: ChatConnectionEvents,
        source: ILiveChatSource,
        channel: ChannelInfo?
    ) {
        if (!Flag.CHAT_HISTORY.asBoolean()) {
            return
        }

        channel?.let { info ->
            if (event is ChatConnectionEvents.ChatConnectingEvent && info.id == event.getChannelId()) {
                injectTwitchChatHistory(
                    source = source,
                    channelName = info.name,
                    channelId = event.channelId
                )
            }
        }
    }

    private fun injectTwitchChatHistory(
        source: ILiveChatSource,
        channelName: String?,
        channelId: Int
    ) {
        if (channelName.isNullOrBlank()) {
            return
        }

        source.addChatHistoryMessage(
            repository.getSystemMessage(
                text = "[Twitch] ${rm.getString("orange_chat_history_fetching")}"
            ),
            channelId
        )

        source.addDisposable(
            repository.getMessages(channelName = channelName)
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ messages ->
                    if (messages.isNullOrEmpty()) {
                        return@subscribe
                    }

                    messages.forEach { message ->
                        source.addChatHistoryMessage(message, channelId)
                    }
                }) {
                    it.printStackTrace()
                    source.addChatHistoryMessage(
                        repository.getSystemMessage(
                            text = rm.getString(
                                "orange_generic_error_d",
                                "Twitch",
                                it.localizedMessage ?: "<empty>"
                            )
                        ),
                        channelId
                    )
                }
        )
    }

    override fun onCreateFeature() {}
}
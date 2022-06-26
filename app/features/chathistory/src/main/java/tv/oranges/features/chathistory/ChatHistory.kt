package tv.oranges.features.chathistory

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.oranges.features.chathistory.bridge.ILiveChatSource
import tv.oranges.features.chathistory.di.component.DaggerChatHistoryComponent
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.events.ChatConnectionEvents
import javax.inject.Inject

class ChatHistory @Inject constructor() {
    fun requestChatHistory(
        event: ChatConnectionEvents,
        source: ILiveChatSource,
        channel: ChannelInfo?
    ) {
        channel?.let { info ->
            if (event is ChatConnectionEvents.ChatConnectingEvent) {
                if (info.id == event.getChannelId()) {
                    injectChatHistory(source = source, channelName = info.name)
                }
            }
        }
    }

    private fun injectChatHistory(source: ILiveChatSource, channelName: String?) {
        if (channelName.isNullOrBlank()) {
            return
        }

//        source.addDisposable(
//            asyncNetRequest(loader.injector.twitchRepository.getChatHistory(channelName = channelName))
//                .observeOn(AndroidSchedulers.mainThread()).subscribe({
//                    if (it.isNullOrEmpty()) {
//                        source.addChatHistoryMessage(getString("mod_chat_history_no_messages"))
//                        return@subscribe
//                    }
//                    source.addChatHistoryMessages(it)
//                }) {
//                    reportException(th = it, where = "HooksDelegate.injectChatHistory")
//                    source.addChatHistoryMessage(getString("mod_chat_history_no_messages"))
//                })
    }

    companion object {
        private val INSTANCE by lazy {
            val hook = DaggerChatHistoryComponent.builder()
                .coreComponent(Core.getInjector().provideComponent(CoreComponent::class))
                .build().chatHistory

            Logger.debug("created: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): ChatHistory {
            return INSTANCE
        }
    }
}
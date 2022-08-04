package tv.orange.features.chathistory.bridge

import io.reactivex.disposables.Disposable
import tv.twitch.chat.ChatMessageInfo

interface ILiveChatSource {
    fun addDisposable(disposable: Disposable)
    fun addChatHistoryMessage(message: ChatMessageInfo, channelId: Int)
}
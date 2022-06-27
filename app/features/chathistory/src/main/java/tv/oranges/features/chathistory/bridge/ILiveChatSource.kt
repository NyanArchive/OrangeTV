package tv.oranges.features.chathistory.bridge

import io.reactivex.disposables.Disposable
import tv.twitch.android.shared.chat.parser.ExtensionMessageKt

interface ILiveChatSource {
    fun addDisposable(disposable: Disposable)
    fun addChatHistoryMessage(channelId: Int, message: ExtensionMessageKt)
    fun addChatHistoryMessages(channelId: Int, messages: List<ExtensionMessageKt>)
}
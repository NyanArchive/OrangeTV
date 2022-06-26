package tv.oranges.features.chathistory.bridge

import io.reactivex.disposables.Disposable

interface ILiveChatSource {
    fun addDisposable(disposable: Disposable)
    fun addChatHistoryMessage(line: String)
}
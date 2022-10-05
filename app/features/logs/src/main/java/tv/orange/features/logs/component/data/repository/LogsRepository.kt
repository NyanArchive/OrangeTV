package tv.orange.features.logs.component.data.repository

import io.reactivex.Single
import tv.orange.features.logs.component.data.model.MessageInfo
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.source.LocalLogsDataSource
import tv.orange.features.logs.component.data.source.TwitchLogsDataSource
import tv.twitch.android.provider.chat.model.ChatHistoryMessage
import javax.inject.Inject

class LogsRepository @Inject constructor(
    val twitchLogsDataSource: TwitchLogsDataSource,
    val localLogsDataSource: LocalLogsDataSource
) {
    fun getLocalLogs(userId: Int, channelId: Int): Single<List<MessageItem>> {
        return localLogsDataSource.getMessages(userId = userId, channelId = channelId)
    }

    fun getTwitchLogs(userLogin: String, channelId: String): Single<List<MessageItem>> {
        return twitchLogsDataSource.getMessages(userLogin = userLogin, channelId = channelId)
    }

    fun addLocalMessage(
        channelId: Int,
        userId: Int,
        userName: String,
        timestamp: Int,
        msg: ChatHistoryMessage
    ) {
        localLogsDataSource.addMessage(MessageInfo(
            userId = userId,
            userName = userName,
            timestamp = timestamp,
            msg = msg,
            channelId = channelId
        ))
    }
}
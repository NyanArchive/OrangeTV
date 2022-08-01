package tv.orange.features.logs.component.data.repository

import io.reactivex.Single
import tv.orange.features.logs.component.data.model.ChatMessage
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.source.LogsDataSource
import tv.orange.features.logs.di.scope.LogsScope
import javax.inject.Inject

@LogsScope
class LogsRepository @Inject constructor(val logsDataSource: LogsDataSource) {
    fun getLogs(userLogin: String, channelId: String): Single<List<MessageItem>> {
        return logsDataSource.getMessages(userLogin, channelId)
    }
}
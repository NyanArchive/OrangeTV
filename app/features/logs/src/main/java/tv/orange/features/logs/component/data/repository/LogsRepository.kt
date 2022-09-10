package tv.orange.features.logs.component.data.repository

import io.reactivex.Single
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.source.LogsDataSource
import javax.inject.Inject

class LogsRepository @Inject constructor(val logsDataSource: LogsDataSource) {
    fun getLogs(userLogin: String, channelId: String): Single<List<MessageItem>> {
        return logsDataSource.getMessages(userLogin = userLogin, channelId = channelId)
    }
}
package tv.orange.features.logs.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.logs.component.data.mapper.LogsMapper
import tv.orange.features.logs.component.data.model.MessageInfo
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.provider.LocalLogsProvider
import javax.inject.Inject

class LocalLogsDataSource @Inject constructor(
    val mapper: LogsMapper
) {
    fun getMessages(userId: Int, channelId: Int): Single<List<MessageItem>> {
        return Single.just(
            mapper.mapLocalLogs(
                LocalLogsProvider.getMessages(
                    channelId = channelId,
                    userId = userId
                ).asReversed()
            )
        ).subscribeOn(Schedulers.io())
    }

    fun addMessage(messageInfo: MessageInfo) {
        LocalLogsProvider.addMessage(messageInfo)
    }
}
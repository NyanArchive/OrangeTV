package tv.orange.features.logs.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.repository.TwitchRepository
import tv.orange.features.logs.component.data.mapper.LogsMapper
import tv.orange.features.logs.component.data.model.MessageInfo
import tv.orange.features.logs.component.data.model.MessageItem
import tv.orange.features.logs.component.data.provider.LocalLogsProvider
import javax.inject.Inject

class LocalLogsDataSource @Inject constructor(
    val twitchRepository: TwitchRepository,
    val mapper: LogsMapper
) {
    fun getMessages(userName: String, channelId: Int): Single<List<MessageItem>> {
        return twitchRepository.getUserInfo(login = userName).flatMap { userInfo ->
            return@flatMap getMessages(
                userId = userInfo.userId.toIntOrNull() ?: -1,
                channelId = channelId
            )
        }.subscribeOn(Schedulers.io())
    }

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
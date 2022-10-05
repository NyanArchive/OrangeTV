package tv.orange.features.logs.component.data.provider

import tv.orange.core.models.flag.Flag
import tv.orange.features.logs.component.data.model.MessageInfo
import java.util.concurrent.ConcurrentLinkedQueue

object LocalLogsProvider {
    private val logs = LimitedQueue<MessageInfo>()

    fun addMessage(messageDesc: MessageInfo) {
        if (!logs.contains(messageDesc)) {
            logs.add(messageDesc)
        }
    }

    fun getMessages(channelId: Int, userId: Int): List<MessageInfo> =
        logs.filter { it.userId == userId && it.channelId == channelId }.toList()

    class LimitedQueue<E> : ConcurrentLinkedQueue<E>() {
        override fun offer(e: E): Boolean {
            val offer = super.offer(e)
            while (offer && size > Flag.LOCAL_LOGS.valueHolder.value.toString().toInt()) {
                super.poll()
            }
            return offer
        }
    }
}
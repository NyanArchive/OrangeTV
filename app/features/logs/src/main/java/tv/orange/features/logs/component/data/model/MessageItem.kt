package tv.orange.features.logs.component.data.model

import java.util.*

interface MessageItem {
    data class Content(val content: ChatMessage) : MessageItem
    data class Header(val date: Date) : MessageItem
}
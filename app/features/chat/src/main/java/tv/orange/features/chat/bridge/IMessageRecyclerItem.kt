package tv.orange.features.chat.bridge

interface IMessageRecyclerItem {
    fun setHighlightColor(highlightColor: Int?)
    fun getHighlightColor(): Int?
    fun markAsDeleted()
    fun getHasBeenDeleted(): Boolean
}
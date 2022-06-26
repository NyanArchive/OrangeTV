package tv.oranges.features.chathistory.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.oranges.features.chathistory.ChatHistory
import tv.oranges.features.chathistory.di.module.ChatHistoryModule
import tv.oranges.features.chathistory.di.scope.ChatHistoryScope

@ChatHistoryScope
@Component(dependencies = [CoreComponent::class], modules = [ChatHistoryModule::class])
interface ChatHistoryComponent {
    val chatHistory: ChatHistory
}
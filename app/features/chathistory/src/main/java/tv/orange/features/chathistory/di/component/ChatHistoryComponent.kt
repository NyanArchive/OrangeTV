package tv.orange.features.chathistory.di.component

import dagger.BindsInstance
import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.chathistory.ChatHistory
import tv.orange.features.chathistory.di.module.ChatHistoryModule
import tv.orange.features.chathistory.di.scope.ChatHistoryScope
import tv.twitch.android.network.graphql.GraphQlService

@ChatHistoryScope
@Component(dependencies = [CoreComponent::class], modules = [ChatHistoryModule::class])
interface ChatHistoryComponent {
    val chatHistory: ChatHistory

    @Component.Factory
    interface Factory {
        fun create(
            coreComponent: CoreComponent,
            @BindsInstance service: GraphQlService
        ): ChatHistoryComponent
    }
}
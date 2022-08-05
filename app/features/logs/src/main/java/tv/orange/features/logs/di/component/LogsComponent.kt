package tv.orange.features.logs.di.component

import dagger.BindsInstance
import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.logs.ChatLogs
import tv.orange.features.logs.data.view.LogsFragment
import tv.orange.features.logs.di.module.LogsModule
import tv.orange.features.logs.di.scope.LogsScope
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory

@LogsScope
@Component(dependencies = [CoreComponent::class], modules = [LogsModule::class])
interface LogsComponent {
    val chatLogs: ChatLogs
    val logsFragment: LogsFragment

    @Component.Factory
    interface Factory {
        fun create(
            coreComponent: CoreComponent,
            @BindsInstance factory: ChatMessageFactory.Factory,
            @BindsInstance service: GraphQlService
        ): LogsComponent
    }
}
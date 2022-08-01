package tv.orange.features.chapters.di.component

import dagger.BindsInstance
import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.chapters.ChaptersHook
import tv.orange.features.chapters.data.view.ChaptersFragment
import tv.orange.features.chapters.di.module.ChaptersModule
import tv.orange.features.chapters.di.scope.ChaptersScope
import tv.twitch.android.network.graphql.GraphQlService

@ChaptersScope
@Component(dependencies = [CoreComponent::class], modules = [ChaptersModule::class])
interface ChaptersComponent {
    val hook: ChaptersHook
    val chaptersFragment: ChaptersFragment

    @Component.Factory
    interface Factory {
        fun create(
            coreComponent: CoreComponent,
            @BindsInstance service: GraphQlService
        ): ChaptersComponent
    }
}
package tv.orange.bridge.di.module

import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.api.di.component.DaggerApiComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.badges.di.component.DaggerBadgesComponent
import tv.orange.features.emotes.di.component.DaggerEmotesComponent
import tv.orange.features.emotes.di.component.EmotesComponent

@Module
class BridgeComponentModule {
    @Provides
    fun provideEmotesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): EmotesComponent {
        return DaggerEmotesComponent.builder().coreComponent(coreComponent)
            .apiComponent(apiComponent).build()
    }

    @Provides
    fun provideBadgesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): BadgesComponent {
        return DaggerBadgesComponent.builder().coreComponent(coreComponent)
            .apiComponent(apiComponent).build()
    }

    @BridgeScope
    @Provides
    fun provideApiComponent(coreComponent: CoreComponent): ApiComponent {
        return DaggerApiComponent.factory().create(coreComponent = coreComponent)
    }
}
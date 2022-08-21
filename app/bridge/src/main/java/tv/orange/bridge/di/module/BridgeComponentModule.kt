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
import tv.orange.features.pronouns.di.component.DaggerPronounComponent
import tv.orange.features.pronouns.di.component.PronounComponent

@Module
class BridgeComponentModule {
    @Provides
    fun provideEmotesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): EmotesComponent {
        return DaggerEmotesComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent).build()
    }

    @Provides
    fun providePronounsComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): PronounComponent {
        return DaggerPronounComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent).build()
    }

    @Provides
    fun provideBadgesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): BadgesComponent {
        return DaggerBadgesComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent).build()
    }

    @BridgeScope
    @Provides
    fun provideApiComponent(coreComponent: CoreComponent): ApiComponent {
        return DaggerApiComponent.builder()
            .coreComponent(coreComponent).build()
    }
}
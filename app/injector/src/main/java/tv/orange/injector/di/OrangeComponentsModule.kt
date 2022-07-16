package tv.orange.injector.di

import dagger.Module
import dagger.Provides
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.di.component.DaggerCoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.api.di.component.DaggerApiComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.badges.di.component.DaggerBadgesComponent
import tv.orange.features.emotes.di.component.DaggerEmotesComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.twitch.android.app.core.ApplicationContext

@Module
class OrangeComponentsModule {
    @Provides
    fun provideBadgesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): BadgesComponent {
        return DaggerBadgesComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent)
            .build()
    }

    @Provides
    fun provideEmotesComponent(
        coreComponent: CoreComponent,
        apiComponent: ApiComponent
    ): EmotesComponent {
        return DaggerEmotesComponent.builder()
            .coreComponent(coreComponent)
            .apiComponent(apiComponent)
            .build()
    }

    @Provides
    @InjectorScope
    fun provideCoreComponent(): CoreComponent {
        return DaggerCoreComponent.factory().create(ApplicationContext.getInstance().getContext())
    }

    @Provides
    @InjectorScope
    fun provideApiComponent(coreComponent: CoreComponent): ApiComponent {
        return DaggerApiComponent.factory().create(coreComponent)
    }
}
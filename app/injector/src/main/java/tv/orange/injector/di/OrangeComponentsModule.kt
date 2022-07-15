package tv.orange.injector.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.di.component.DaggerCoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.api.di.component.DaggerApiComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.badges.di.component.DaggerBadgesComponent
import tv.orange.features.emotes.di.component.DaggerEmotesComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.models.ProtoComponent
import tv.twitch.android.app.core.ApplicationContext

@Module
class OrangeComponentsModule {
    @Volatile
    private var coreComponentInstance: CoreComponent? = null

    @Provides
    @IntoMap
    @KClassKey(CoreComponent::class)
    fun provideCoreComponent(): CoreComponent {
        coreComponentInstance?.let {
            return it
        }

        synchronized(this) {
            coreComponentInstance ?: run {
                coreComponentInstance = DaggerCoreComponent.factory()
                    .create(ApplicationContext.getInstance().context)

                Logger.debug("Provide new instance: $coreComponentInstance")
            }
        }

        return coreComponentInstance!!
    }

    @Provides
    @IntoMap
    @KClassKey(BadgesComponent::class)
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
    @IntoMap
    @KClassKey(EmotesComponent::class)
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
    @IntoMap
    @InjectorScope
    @KClassKey(ApiComponent::class)
    fun provideApiComponent(coreComponent: CoreComponent): ProtoComponent {
        return DaggerApiComponent.factory().create(coreComponent)
    }
}
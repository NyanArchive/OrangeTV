package tv.orange.injector

import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.di.component.DaggerCoreComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.badges.di.component.DaggerBadgesComponent
import tv.orange.features.badges.di.module.BadgesApiModule
import tv.orange.features.emotes.di.component.DaggerEmotesComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.features.emotes.di.module.EmotesApiModule
import tv.orange.models.Injector
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.app.core.ApplicationContext
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Provider
import kotlin.reflect.KClass

class Injector(private val twitchComponent: DaggerAppComponent) : Injector {
    @Volatile
    private var coreComponentInstance: CoreComponent? = null

    private val coreComponent: CoreComponent
        get() = kotlin.run {
            coreComponentInstance?.let {
                return it
            }

            synchronized(this) {
                coreComponentInstance ?: run {
                    coreComponentInstance = DaggerCoreComponent.factory()
                        .create(ApplicationContext.getInstance().context)
                    Logger.debug("CoreComponent: $coreComponentInstance")
                }
            }

            return coreComponentInstance!!
        }

    private fun provideBadges(): BadgesComponent {
        return DaggerBadgesComponent.builder()
            .coreComponent(coreComponent)
            .badgesApiModule(BadgesApiModule())
            .build()
    }

    private fun provideEmotes(): EmotesComponent {
        return DaggerEmotesComponent.builder()
            .coreComponent(coreComponent)
            .emotesApiModule(EmotesApiModule())
            .build()
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun <T : Any> provideComponent(cls: KClass<T>): T {
        return when (cls) {
            CoreComponent::class -> coreComponent
            BadgesComponent::class -> provideBadges()
            EmotesComponent::class -> provideEmotes()
            else -> throw IllegalStateException("Unknown class: $cls")
        } as T
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    override fun <T : Any> provideTwitchComponent(cls: KClass<T>): T {
        return when (cls) {
            GraphQlService::class -> provideTwitchGraphQlService()
            else -> throw IllegalStateException("Unknown class: $cls")
        } as T
    }

    private fun provideTwitchGraphQlService(): GraphQlService {
        return getProvider<GraphQlService>(twitchComponent, "graphQlServiceProvider").get()
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun <T> getProvider(component: DaggerAppComponent, fieldName: String): Provider<T> {
            return component::class.java.getDeclaredField(fieldName).apply {
                isAccessible = true
            }.get(component) as Provider<T>
        }
    }
}
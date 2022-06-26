package tv.orange.injector

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
import tv.twitch.android.network.graphql.TwitchApolloClient
import javax.inject.Provider
import kotlin.reflect.KClass

class Injector(private val twitchComponent: DaggerAppComponent) : Injector {
    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(ApplicationContext.getInstance().context)
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
            TwitchApolloClient::class -> provideTwitchApolloClient()
            else -> throw IllegalStateException("Unknown class: $cls")
        } as T
    }

    @Suppress("UNCHECKED_CAST")
    private fun provideTwitchApolloClient(): TwitchApolloClient {
        val field =
            twitchComponent::class.java.getDeclaredField("provideApolloGqlOkHttpClientProvider")
                .apply {
                    isAccessible = true
                }
        val value = field.get(twitchComponent) as Provider<TwitchApolloClient>
        return value.get()
    }
}
package tv.orange.injector

import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.injector.di.DaggerInjectorComponent
import tv.orange.injector.di.InjectorScope
import tv.orange.models.Injector
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

@InjectorScope
class Injector @Inject constructor(
    val twitchComponent: DaggerAppComponent,
    val coreComponent: Provider<CoreComponent>,
    val apiComponent: Provider<ApiComponent>,
    val emotesComponent: Provider<EmotesComponent>,
    val badgesComponent: Provider<BadgesComponent>
) : Injector {
    private val map = mutableMapOf<KClass<*>, Provider<*>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T> {
        return map[cls] as Provider<T>
    }

    private fun <T : Any> register(cls: KClass<T>, provider: Provider<T>) {
        map[cls] = provider
    }

    private fun initialize() {
        register(CoreComponent::class) { coreComponent.get() }
        register(ApiComponent::class) { apiComponent.get() }
        register(EmotesComponent::class) { emotesComponent.get() }
        register(BadgesComponent::class) { badgesComponent.get() }
        register(GraphQlService::class) {
            getTwitchProvider<GraphQlService>(
                twitchComponent,
                "graphQlServiceProvider"
            ).get()
        }
    }

    companion object {
        @JvmStatic
        fun create(twitchComponent: DaggerAppComponent): tv.orange.injector.Injector {
            return DaggerInjectorComponent.factory().create(twitchComponent).injector.apply {
                initialize()
            }
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> getTwitchProvider(component: DaggerAppComponent, fieldName: String): Provider<T> {
            return component::class.java.getDeclaredField(fieldName).apply {
                isAccessible = true
            }.get(component) as Provider<T>
        }
    }
}
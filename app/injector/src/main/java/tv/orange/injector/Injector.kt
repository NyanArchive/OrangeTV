package tv.orange.injector

import tv.orange.models.Injector
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import javax.inject.Provider
import kotlin.reflect.KClass

class Injector private constructor(private val twitchComponent: DaggerAppComponent) : Injector {
    private val map = mutableMapOf<KClass<*>, Provider<*>>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T> {
        return map[cls] as Provider<T>
    }

    private fun <T : Any> register(cls: KClass<T>, provider: Provider<T>) {
        map[cls] = provider
    }

    fun initialize() {
        register(GraphQlService::class) {
            getTwitchProvider<GraphQlService>(
                twitchComponent,
                "graphQlServiceProvider"
            ).get()
        }
        register(ChatMessageFactory.Factory::class) {
            getTwitchProvider<ChatMessageFactory.Factory>(
                twitchComponent,
                "factoryProvider2"
            ).get()
        }
    }

    companion object {
        @JvmStatic
        fun create(twitchComponent: DaggerAppComponent): tv.orange.injector.Injector {
            return Injector(twitchComponent)
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> getTwitchProvider(
            component: DaggerAppComponent,
            fieldName: String
        ): Provider<T> {
            return component::class.java.getDeclaredField(fieldName).apply {
                isAccessible = true
            }.get(component) as Provider<T>
        }
    }
}
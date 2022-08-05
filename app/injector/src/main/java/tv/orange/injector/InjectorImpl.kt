package tv.orange.injector

import tv.orange.models.abc.Injector
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider
import kotlin.reflect.KClass

class InjectorImpl private constructor() : Injector {
    private val map = ConcurrentHashMap<KClass<*>, Provider<*>>(2)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T> {
        return map[cls] as Provider<T>
    }

    private fun <T : Any> register(cls: KClass<T>, provider: Provider<T>) {
        map[cls] = provider
    }

    fun initialize(appComponent: DaggerAppComponent) {
        register(GraphQlService::class) {
            getTwitchProvider<GraphQlService>(
                appComponent,
                "graphQlServiceProvider"
            ).get()
        }
        register(ChatMessageFactory.Factory::class) {
            getTwitchProvider<ChatMessageFactory.Factory>(
                appComponent,
                "factoryProvider2"
            ).get()
        }
    }

    companion object {
        @JvmStatic
        fun create(): InjectorImpl {
            return InjectorImpl()
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
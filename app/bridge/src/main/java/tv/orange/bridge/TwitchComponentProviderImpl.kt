package tv.orange.bridge

import tv.orange.core.compat.ClassCompat.getPrivateField
import tv.orange.models.abc.TwitchComponentProvider
import tv.twitch.android.app.consumer.dagger.AppComponent
import tv.twitch.android.network.graphql.GraphQlService
import tv.twitch.android.shared.chat.messagefactory.ChatMessageFactory
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Provider
import kotlin.reflect.KClass

class TwitchComponentProviderImpl private constructor() : TwitchComponentProvider {
    private val factory = ConcurrentHashMap<KClass<*>, Provider<*>>(2)

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getProvider(cls: KClass<T>): Provider<T> {
        return factory[cls] as Provider<T>
    }

    private fun <T : Any> register(clazz: KClass<T>, provider: Provider<T>) {
        factory[clazz] = provider
    }

    fun initialize(appComponent: AppComponent) {
        register(
            clazz = GraphQlService::class,
            provider = appComponent.getPrivateField("graphQlServiceProvider")
        )
        register(
            clazz = ChatMessageFactory.Factory::class,
            provider = appComponent.getPrivateField("factoryProvider")
        )
    }

    companion object {
        @JvmStatic
        fun create(): TwitchComponentProviderImpl {
            return TwitchComponentProviderImpl()
        }
    }
}
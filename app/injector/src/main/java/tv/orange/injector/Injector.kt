package tv.orange.injector

import tv.orange.injector.di.DaggerInjectorComponent
import tv.orange.models.Injector
import tv.orange.models.ProtoComponent
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Provider
import kotlin.reflect.KClass

class Injector(private val twitchComponent: DaggerAppComponent) : Injector {
    private val injectorComponent by lazy {
        DaggerInjectorComponent.create()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> provideComponent(cls: KClass<T>): T {
        if (cls !is ProtoComponent) {
            throw IllegalStateException("Can't provide non proto components: $cls")
        }

        val obj = injectorComponent.componentByClass()[cls.java as Class<out ProtoComponent>]
            ?: throw IllegalStateException("Unknown class: $cls")

        return obj as T
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
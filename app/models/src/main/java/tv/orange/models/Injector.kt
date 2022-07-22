package tv.orange.models

import javax.inject.Provider
import kotlin.reflect.KClass

interface Injector {
    fun <T : Any> provideComponent(cls: KClass<T>): Provider<T>
}
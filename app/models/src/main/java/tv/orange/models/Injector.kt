package tv.orange.models

import kotlin.reflect.KClass

interface Injector {
    fun <T : Any> provideComponent(cls: KClass<T>): T
}
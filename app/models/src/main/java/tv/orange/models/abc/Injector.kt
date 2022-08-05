package tv.orange.models.abc

import javax.inject.Provider
import kotlin.reflect.KClass

interface Injector {
    fun <T : Any> getComponentProvider(cls: KClass<T>): Provider<T>
}
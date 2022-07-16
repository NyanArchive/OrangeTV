package tv.orange.models

import javax.inject.Provider
import kotlin.reflect.KClass

interface Injector {
    fun <T : Any> provideComponent(cls: KClass<T>): Provider<T>
    fun <T : Any> provideTwitchComponent(cls: KClass<T>): Provider<T>
}
package tv.orange.models.abc

import javax.inject.Provider
import kotlin.reflect.KClass

interface TwitchComponentProvider {
    fun <T : Any> getProvider(cls: KClass<T>): Provider<T>
}
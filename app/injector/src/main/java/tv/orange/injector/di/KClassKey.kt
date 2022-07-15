package tv.orange.injector.di

import dagger.MapKey
import tv.orange.models.ProtoComponent
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
annotation class KClassKey(val value: KClass<out ProtoComponent>)
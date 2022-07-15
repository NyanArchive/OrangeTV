package tv.orange.injector.di

import dagger.Component
import tv.orange.models.ProtoComponent

@Component(modules = [OrangeComponentsModule::class])
@InjectorScope
interface InjectorComponent {
    fun componentByClass(): Map<Class<out ProtoComponent>, ProtoComponent>

    @Component.Factory
    interface Factory {
        fun create(): InjectorComponent
    }
}
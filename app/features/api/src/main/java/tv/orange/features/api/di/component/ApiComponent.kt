package tv.orange.features.api.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.component.repository.*
import tv.orange.features.api.di.module.ApiModule
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.ProtoComponent

@ApiScope
@Component(dependencies = [CoreComponent::class], modules = [ApiModule::class])
interface ApiComponent : ProtoComponent {
    val stvRepository: StvRepository
    val bttvRepository: BttvRepository
    val ffzRepository: FfzRepository
    val chatterinoRepository: ChatterinoRepository
    val nopRepository: NopRepository

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): ApiComponent
    }
}
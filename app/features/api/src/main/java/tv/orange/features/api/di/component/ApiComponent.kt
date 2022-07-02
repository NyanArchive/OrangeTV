package tv.orange.features.api.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.component.repository.BttvRepository
import tv.orange.features.api.component.repository.FfzRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.api.di.module.ApiModule
import tv.orange.features.api.di.scope.ApiScope

@ApiScope
@Component(dependencies = [CoreComponent::class], modules = [ApiModule::class])
interface ApiComponent {
    val stvRepository: StvRepository
    val bttvRepository: BttvRepository
    val ffzRepository: FfzRepository

    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): ApiComponent
    }
}
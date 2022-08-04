package tv.orange.features.stv.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.stv.StvAvatars
import tv.orange.features.stv.di.module.StvModule
import tv.orange.features.stv.di.scope.StvScope

@StvScope
@Component(dependencies = [CoreComponent::class, ApiComponent::class], modules = [StvModule::class])
interface StvComponent {
    val stvAvatars: StvAvatars
}
package tv.orange.features.vodsync.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.vodsync.VodSync
import tv.orange.features.vodsync.di.module.VodSyncModule
import tv.orange.features.vodsync.di.scope.VodSyncScope

@VodSyncScope
@Component(dependencies = [CoreComponent::class], modules = [VodSyncModule::class])
interface VodSyncComponent {
    val vodSync: VodSync
}
package tv.orange.features.tracking.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.tracking.Tracking
import tv.orange.features.tracking.di.module.TrackingModule
import tv.orange.features.tracking.di.scope.TrackingScope

@TrackingScope
@Component(
    dependencies = [CoreComponent::class, ApiComponent::class],
    modules = [TrackingModule::class]
)
interface TrackingComponent {
    val tracking: Tracking
}
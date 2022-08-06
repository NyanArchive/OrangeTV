package tv.orange.features.ui.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.ui.UI
import tv.orange.features.ui.di.module.UIModule
import tv.orange.features.ui.di.scope.UIScope

@UIScope
@Component(dependencies = [CoreComponent::class], modules = [UIModule::class])
interface UIComponent {
    val ui: UI
}
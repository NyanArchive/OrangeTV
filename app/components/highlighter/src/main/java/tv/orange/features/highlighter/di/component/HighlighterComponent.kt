package tv.orange.features.highlighter.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.highlighter.Highlighter
import tv.orange.features.highlighter.di.module.HighlighterModule
import tv.orange.features.highlighter.di.scope.HighlighterScope

@HighlighterScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [HighlighterModule::class]
)
interface HighlighterComponent {
    val highlighter: Highlighter
}
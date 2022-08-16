package tv.orange.features.chat.di.component

import dagger.Component
import tv.orange.core.di.component.CoreComponent
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.chat.ChatHookProvider
import tv.orange.features.chat.di.module.ChatModule
import tv.orange.features.chat.di.scope.ChatScope
import tv.orange.features.emotes.di.component.EmotesComponent
import tv.orange.features.pronouns.di.component.PronounComponent

@ChatScope
@Component(
    dependencies = [CoreComponent::class,
        EmotesComponent::class,
        BadgesComponent::class,
        PronounComponent::class],
    modules = [ChatModule::class]
)
interface ChatComponent {
    val hook: ChatHookProvider
}
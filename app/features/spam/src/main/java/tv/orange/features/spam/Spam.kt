package tv.orange.features.spam

import tv.orange.core.Core
import tv.orange.core.compat.ClassCompat.getPrivateField
import tv.orange.features.spam.component.factory.ChatCommandInterceptorFactory
import tv.orange.features.spam.di.scope.SpamScope
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import tv.twitch.android.shared.chat.command.VoteCommandInterceptor
import javax.inject.Inject

@SpamScope
class Spam @Inject constructor(
    val factory: ChatCommandInterceptorFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Spam::class.java)
    }

    fun createSpamCommandInterceptor(interceptor: VoteCommandInterceptor): ChatCommandInterceptor {
        return factory.createSpamCommandInterceptor(
            chatSource = interceptor.getPrivateField("liveChatSource")
        )
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
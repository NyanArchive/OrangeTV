package tv.orange.features.emotes

import tv.orange.core.Logger
import tv.orange.core.models.LifecycleAware
import tv.orange.features.core.CoreFeature
import tv.orange.features.emotes.component.EmoteProvider
import tv.orange.features.emotes.di.ApiModule
import tv.orange.features.emotes.di.DaggerEmotesComponent
import tv.orange.features.emotes.di.EmotesScope
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

@EmotesScope
class Hook @Inject constructor(val emoteProvider: EmoteProvider): LifecycleAware {
    fun injectEmotes(
        chatMessageInterface: ChatMessageInterface,
        channelId: Int
    ): ChatMessageInterface {
        // Logger.debug("$chatMessageInterface, $channelId")
        return chatMessageInterface
    }

    companion object {
        private val INSTANCE by lazy {
            val hook = DaggerEmotesComponent.builder()
                .apiModule(ApiModule())
                .coreFeatureComponent(CoreFeature.get().component)
                .build().hook
            Logger.debug("created: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): Hook {
            return INSTANCE
        }
    }

    private fun requestChannelEmotes(channelId: Int?) {
        channelId ?: return

        if (channelId <= 0) {
            return
        }

        emoteProvider.requestChannelEmotes(channelId)
    }

    override fun onAllComponentDestroyed() {
        emoteProvider.clear()
    }

    override fun onAllComponentStopped() {
    }

    override fun onSdkResume() {
        emoteProvider.updateEmotes()
    }

    override fun onAccountLogout() {
    }

    override fun onFirstActivityCreated() {
        emoteProvider.fetchEmotes()
    }

    override fun onFirstActivityStarted() {
    }

    override fun onConnectedToChannel(channelId: Int) {
        requestChannelEmotes(channelId)
    }
}
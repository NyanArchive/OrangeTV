package tv.orange.features.badges

import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.LifecycleAware
import tv.orange.features.badges.bridge.OrangeMessageBadge
import tv.orange.features.badges.component.BadgeProvider
import tv.orange.features.badges.di.component.BadgesComponent
import tv.orange.features.badges.di.scope.BadgesScope
import tv.twitch.android.models.chat.MessageBadge
import javax.inject.Inject

@BadgesScope
class BadgesInjector @Inject constructor(val badgeProvider: BadgeProvider) : LifecycleAware {
    fun injectBadges(
        badges: List<MessageBadge>,
        userId: Int,
        channelId: Int
    ): List<MessageBadge> {
        val newBadges = badgeProvider.getBadges(userId).toMutableList()
        if (newBadges.isEmpty()) {
            return badges
        }

        val stack = mutableListOf<MessageBadge>()
        badges.forEach { badge ->
            val replaces = newBadges.firstOrNull { it.getReplaces() == badge.name }

            if (replaces != null) {
                stack.add(
                    OrangeMessageBadge(
                        replaces.getCode(),
                        replaces.getUrl(),
                        replaces.getBackgroundColor()
                    )
                )
                newBadges.remove(replaces)
            } else {
                stack.add(badge)
            }
        }
        stack.addAll(newBadges.map {
            OrangeMessageBadge(it.getCode(), it.getUrl(), it.getBackgroundColor())
        })

        return stack
    }

    companion object {
        private val INSTANCE by lazy {
            val hook = Core.getInjector().provideComponent(BadgesComponent::class).badgesInjector

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): BadgesInjector {
            return INSTANCE
        }
    }

    override fun onAllComponentDestroyed() {
        badgeProvider.clear()
    }

    override fun onAllComponentStopped() {}

    override fun onSdkResume() {}

    override fun onAccountLogout() {}

    override fun onFirstActivityCreated() {
        badgeProvider.fetchBadges()
    }

    override fun onFirstActivityStarted() {}

    override fun onConnectedToChannel(channelId: Int) {}
    override fun onConnectingToChannel(channelId: Int) {}
}
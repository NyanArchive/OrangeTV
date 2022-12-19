package tv.orange.features.badges.component.model

import tv.orange.core.LoggerWithTag
import tv.orange.models.abc.BadgePackageSet
import tv.orange.models.data.SimpleFetcher
import tv.orange.models.data.badges.Badge
import tv.orange.models.data.badges.impl.BadgeItzSet

class BadgePackageItzImpl(
    source: SourceFactory<BadgeItzSet>,
    token: BadgePackageSet
) : SimpleFetcher<BadgeItzSet>(
    dataSourceFactory = source,
    logger = LoggerWithTag(token.toString())
), BadgePackage {
    override fun getBadges(userId: Int): List<Badge> {
        return getData()?.getBadges(userId = userId) ?: emptyList()
    }

    override fun isEmpty(): Boolean {
        return getData()?.isEmpty() ?: true
    }

    override fun hasBadges(userId: Int): Boolean {
        return getData()?.hasBadges(userId = userId) ?: false
    }
}
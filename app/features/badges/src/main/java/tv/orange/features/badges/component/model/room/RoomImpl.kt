package tv.orange.features.badges.component.model.room

import tv.orange.features.badges.component.model.BadgePackage
import tv.orange.models.data.badges.Badge

class RoomImpl : Room {
    private val packages: MutableList<BadgePackage> = mutableListOf()

    override fun add(pack: BadgePackage) {
        packages.add(pack)
    }

    override fun getBadges(userId: Int): List<Badge> {
        return packages.flatMap { pack ->
            pack.getBadges(userId)
        }
    }

    override fun fetch() {
        packages.forEach { pack ->
            pack.refresh(force = true)
        }
    }

    override fun refresh() {
        packages.forEach { pack ->
            pack.refresh(force = false)
        }
    }

    override fun clear() {
        packages.forEach { pack ->
            packages.remove(pack)
            pack.clear()
        }
    }

    override fun hasBadges(userId: Int): Boolean {
        return packages.any { pack ->
            pack.hasBadges(userId)
        }
    }
}
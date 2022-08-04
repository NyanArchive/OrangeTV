package tv.orange.features.badges.component

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.badges.component.model.factory.RoomFactory
import tv.orange.features.badges.di.scope.BadgesScope
import tv.orange.models.data.badges.Badge
import javax.inject.Inject

@BadgesScope
class BadgeProvider @Inject constructor(val roomFactory: RoomFactory) {
    private var global = roomFactory.create()

    private val disposables = CompositeDisposable()

    fun clear() {
        Logger.debug("called")
        disposables.clear()
        global = roomFactory.create()
    }

    fun getBadges(userId: Int): List<Badge> {
        return global.getBadges(userId)
    }

    fun fetchBadges() {
        global.fetch()
    }

    fun refreshBadges() {
        global.refresh()
    }

    fun hasBadges(userId: Int): Boolean {
        return global.hasBadges(userId)
    }

    fun rebuild() {
        clear()
        fetchBadges()
    }
}
package tv.orange.features.badges.component

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.badges.component.model.factory.RoomFactory
import tv.orange.models.data.badges.Badge
import javax.inject.Inject

class BadgeProvider @Inject constructor(val roomFactory: RoomFactory) {
    private val global = roomFactory.create()

    private val disposables = CompositeDisposable()

    private fun fetchGlobalBadges() {
        Logger.debug("fetch")
        global.fetch()
    }

    fun clear() {
        disposables.clear()
    }

    fun getBadges(userId: Int): List<Badge> {
        return global.getBadges(userId)
    }

    fun fetchBadges() {
        fetchGlobalBadges()
    }
}
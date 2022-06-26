package tv.orange.features.badges.component

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.badges.component.repository.BadgeRepository
import tv.orange.features.badges.models.Badge
import tv.orange.features.badges.models.BadgeContainer
import javax.inject.Inject

class BadgeProvider @Inject constructor(val emoteRepository: BadgeRepository) {
    private var global = BadgeContainer()

    private val disposables = CompositeDisposable()

    private fun fetchGlobalBadges() {
        disposables.add(emoteRepository.getFfzBadges().subscribe({ ffz ->
            val set = BadgeContainer(ffz)
            Logger.debug("set: $set")
            global = set
        }, {
            it.printStackTrace()
        }))
    }

    fun clear() {
        disposables.clear()
        global = BadgeContainer()
    }

    fun getBadges(userId: Int): Collection<Badge> {
        return global.getBadges(userId)
    }

    fun fetchBadges() {
        if (global.size == 0) {
            fetchGlobalBadges()
        }
    }
}
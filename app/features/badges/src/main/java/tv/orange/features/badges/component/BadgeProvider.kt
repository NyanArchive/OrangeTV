package tv.orange.features.badges.component

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.api.component.repository.FfzRepository
import tv.orange.features.badges.component.model.BadgeContainer
import tv.orange.models.data.badges.Badge
import javax.inject.Inject

class BadgeProvider @Inject constructor(val ffzRepository: FfzRepository) {
    private var global = BadgeContainer()

    private val disposables = CompositeDisposable()

    private fun fetchGlobalBadges() {
        disposables.add(ffzRepository.getFfzBadges().subscribe({ ffz ->
            val set = BadgeContainer(ffz)

            Logger.debug("New global badges: $set")
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
        if (global.isEmpty()) {
            fetchGlobalBadges()
        }
    }
}
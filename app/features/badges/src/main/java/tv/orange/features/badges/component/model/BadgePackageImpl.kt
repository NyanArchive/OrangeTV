package tv.orange.features.badges.component.model

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.core.util.DateUtil
import tv.orange.features.badges.component.model.factory.BadgeFetcherFactory
import tv.orange.models.abs.BadgePackageSet
import tv.orange.models.data.badges.Badge
import tv.orange.models.data.badges.BadgeSet
import java.util.*

class BadgePackageImpl(
    private val source: BadgeFetcherFactory,
    private val token: BadgePackageSet
) : BadgePackage {
    var set: BadgeSet? = null
    private var lastUpdate: Date = Date(0)
    private var fetching = false

    private val disposables = CompositeDisposable()

    override fun getBadges(userId: Int): List<Badge> {
        return set?.getBadges(userId) ?: emptyList()
    }

    override fun refresh(force: Boolean) {
        Logger.debug("[$token] Starting...")
        if (fetching) {
            Logger.debug("[$token] skip: fetching")
            return
        }

        if (set != null && !force) {
            val diff = DateUtil.toSeconds(DateUtil.getDiff(lastUpdate, Date()))
            if (diff < REFRESH_TIMEOUT) {
                Logger.debug("[$token] skip: diff is $diff")
                return
            }
        }

        fetching = true
        clear()
        disposables.add(source.create().retryWhen { errors ->
            return@retryWhen errors.take(MAX_RETRY_COUNT).doOnNext {
                Logger.debug("[$token] retry...")
            }
        }.subscribe({ set ->
            this.set = set
            this.lastUpdate = Date()
            this.fetching = false
            Logger.debug("[${token}] Fetched: $set")
        }, {
            this.fetching = false
            Logger.debug("[${token}] Cannot fetch badges: ${it.localizedMessage}")
        }))
    }

    override fun isEmpty(): Boolean {
        return set?.isEmpty() ?: true
    }

    override fun clear() {
        disposables.clear()
        set = null
        lastUpdate = Date()
        fetching = false
    }

    override fun hasBadges(userId: Int): Boolean {
        return set?.hasBadges(userId) ?: false
    }

    companion object {
        private const val REFRESH_TIMEOUT = 120
        private const val MAX_RETRY_COUNT: Long = 3
    }
}
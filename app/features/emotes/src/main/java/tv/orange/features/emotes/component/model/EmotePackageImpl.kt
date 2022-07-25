package tv.orange.features.emotes.component.model

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.core.util.DateUtil
import tv.orange.features.emotes.component.model.factory.EmoteFetcherFactory
import tv.orange.models.abs.EmotePackageSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet
import java.util.*

class EmotePackageImpl(
    private val source: EmoteFetcherFactory,
    private val token: EmotePackageSet
) : EmotePackage {
    var set: EmoteSet? = null
    private var lastUpdate: Date = Date(0)
    private var fetching = false

    private val disposables = CompositeDisposable()

    override fun getEmote(code: String): Emote? {
        return set?.getEmote(code)
    }

    override fun getEmotes(): List<Emote> {
        return set?.getEmotes() ?: emptyList()
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
            Logger.debug("[${token}] Cannot fetch emotes: ${it.localizedMessage}")
        }))
    }

    override fun isEmpty(): Boolean {
        return set?.isEmpty() ?: true
    }

    override fun getToken(): EmotePackageSet {
        return token
    }

    override fun clear() {
        disposables.clear()
        set = null
        lastUpdate = Date()
        fetching = false
    }

    companion object {
        private const val REFRESH_TIMEOUT = 120
        private const val MAX_RETRY_COUNT: Long = 2
    }
}
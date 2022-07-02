package tv.orange.features.emotes.component.model

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.core.util.DateUtil
import tv.orange.features.emotes.component.model.factory.EmoteFetcherFactory
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet
import java.util.*

class EmotePackageImpl(
    private val source: EmoteFetcherFactory
) : EmotePackage {
    var set: EmoteSet? = null
    private var lastUpdate: Date = Date(0)

    private val disposables = CompositeDisposable()

    override fun getEmote(code: String): Emote? {
        return set?.getEmote(code)
    }

    override fun getEmotes(): List<Emote> {
        return set?.getEmotes() ?: emptyList()
    }

    override fun refresh(force: Boolean) {
        if (!force) {
            val diff = DateUtil.toSeconds(DateUtil.getDiff(lastUpdate, Date()))
            if (diff < REFRESH_TIMEOUT) {
                Logger.debug("Skip: $diff")
                return
            }
        }

        disposables.add(source.create().subscribe({ set ->
            this.set = set
            this.lastUpdate = Date()
            Logger.debug("[${source}] Fetched: $set")
        }, {
            Logger.debug("[${source}] Cannot fetch emotes: ${it.localizedMessage}")
        }))
    }

    override fun isEmpty(): Boolean {
        return set?.isEmpty() ?: true
    }

    override fun clear() {
        disposables.clear()
    }

    companion object {
        private const val REFRESH_TIMEOUT = 120
    }
}
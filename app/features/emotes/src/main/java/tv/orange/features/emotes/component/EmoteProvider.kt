package tv.orange.features.emotes.component

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.emotes.component.data.models.Cache
import tv.orange.features.emotes.component.data.models.ChannelSet
import tv.orange.features.emotes.component.data.models.EmoteSet
import tv.orange.features.emotes.component.repository.EmoteRepository
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject

class EmoteProvider @Inject constructor(val emoteRepository: EmoteRepository) {
    private var globalBttvSet = EmoteSet(listOf())
    private var globalFfzSet = EmoteSet(listOf())

    private val channelEmotesCache = Cache(3)

    private val disposables = CompositeDisposable()

    private fun fetch(
        request: Single<List<Emote>>,
        invoke: (set: List<Emote>) -> Unit,
        tag: String
    ) {
        disposables.add(request.subscribe({ emotes: List<Emote> ->
            invoke(emotes)
            Logger.debug("[$tag] Emotes fetched: ${emotes.size}")
        }, { ex: Throwable ->
            Logger.error("[$tag] Cannot fetch emotes!")
            ex.printStackTrace()
        }))
    }

    private fun fetchBttvGlobalSet() {
        fetch(emoteRepository.getBttvGlobalEmotes(), { emotes ->
            globalBttvSet = EmoteSet(emotes)
        }, "BTTV-GLOBAL")
    }

    private fun fetchFfzGlobalSet() {
        fetch(emoteRepository.getFfzGlobalEmotes(), { emotes ->
            globalFfzSet = EmoteSet(emotes)
        }, "FFZ-GLOBAL")
    }

    fun clear() {
        disposables.clear()
    }

    fun getEmote(code: String, channelId: Int, userId: Int): Emote? {
        return null
    }

    fun getChannelEmotes(channelId: Int): List<Emote> {
        return channelEmotesCache[channelId]?.allEmotes ?: emptyList()
    }

    fun getUserEmotes(userId: Int): List<Emote> {
        return emptyList()
    }

    fun requestChannelEmotes(channelId: Int) {
        if (channelEmotesCache[channelId] == null) {
            fetchChannelEmotes(channelId)
        }
    }

    private fun fetchChannelEmotes(channelId: Int) {
        disposables.add(
            Single.zip(
                emoteRepository.getBttvChannelEmotes(channelId).subscribeOn(Schedulers.io()),
                emoteRepository.getFfzChannelEmotes(channelId).subscribeOn(Schedulers.io())
            ) { bttvEmotes, ffzEmotes ->
                val set = ChannelSet(
                    bttvEmotes = EmoteSet(bttvEmotes),
                    ffzEmotes = EmoteSet(ffzEmotes)
                )
                Logger.debug("set: $set")
                return@zip set
            }.subscribe({ set ->
                channelEmotesCache.put(channelId, set)
            }, {
                it.printStackTrace()
            })
        )
    }

    fun requestUserEmotes(userId: Int) {

    }

    fun updateEmotes() {
        with(globalFfzSet) {
            if (isEmpty()) {
                fetchFfzGlobalSet()
            }
        }
        with(globalBttvSet) {
            if (isEmpty()) {
                fetchBttvGlobalSet()
            }
        }
    }

    fun fetchEmotes() {
        updateEmotes()
    }
}
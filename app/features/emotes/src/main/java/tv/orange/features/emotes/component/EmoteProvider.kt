package tv.orange.features.emotes.component

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Logger
import tv.orange.features.emotes.component.data.models.EmoteSet
import tv.orange.features.emotes.component.repository.EmoteRepository
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject

class EmoteProvider @Inject constructor(val emoteRepository: EmoteRepository) {
    private var globalBttvSet = EmoteSet(listOf())
    private var globalFfzSet = EmoteSet(listOf())

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

    fun clear() {
        disposables.clear()
    }

    fun getEmote(code: String, channelId: Int, userId: Int): Emote? {
        return null
    }

    fun getChannelEmotes(channelId: Int): List<Emote> {
        return listOf()
    }

    fun getUserEmotes(userId: Int): List<Emote> {
        return listOf()
    }

    fun requestEmotes(channelId: Int) {

    }

    fun requestUserEmotes(userId: Int) {

    }

    fun updateEmotes() {
        with(globalFfzSet) {
            if (isEmpty()) {
                // TODO("FFZ")
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
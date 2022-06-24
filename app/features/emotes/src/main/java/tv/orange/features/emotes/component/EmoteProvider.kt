package tv.orange.features.emotes.component

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.emotes.component.data.models.EmoteContainerCache
import tv.orange.features.emotes.component.data.models.EmoteContainer
import tv.orange.features.emotes.component.data.models.EmoteSet
import tv.orange.features.emotes.component.repository.EmoteRepository
import tv.orange.features.emotes.models.Emote
import javax.inject.Inject

class EmoteProvider @Inject constructor(val emoteRepository: EmoteRepository) {
    private var global = EmoteContainer()
    private val channelEmotesCache = EmoteContainerCache(3)

    private val disposables = CompositeDisposable()

    private fun fetchGlobalEmotes() {
        disposables.add(Single.zip(
            emoteRepository.getBttvGlobalEmotes(),
            emoteRepository.getFfzGlobalEmotes(),
            emoteRepository.getStvGlobalEmotes()
        ) { bttv, ffz, stv ->
            val set = EmoteContainer(
                bttvEmotes = EmoteSet(bttv),
                ffzEmotes = EmoteSet(ffz),
                stvEmotes = EmoteSet(stv)
            )
            Logger.debug("set: $set")
            return@zip set
        }.subscribe({ set ->
            global = set
        }, {
            it.printStackTrace()
        })
        )
    }

    fun clear() {
        disposables.clear()
        global = createEmptySet()
    }

    private fun getGlobalEmote(code: String): Emote? {
        return global.getEmote(code)
    }

    fun getEmote(code: String, channelId: Int, userId: Int): Emote? {
        return channelEmotesCache[channelId]?.getEmote(code) ?: getGlobalEmote(code)
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
                emoteRepository.getFfzChannelEmotes(channelId).subscribeOn(Schedulers.io()),
                emoteRepository.getStvChannelEmotes(channelId).subscribeOn(Schedulers.io())
            ) { bttv, ffz, stv ->
                val set = EmoteContainer(
                    bttvEmotes = EmoteSet(bttv),
                    ffzEmotes = EmoteSet(ffz),
                    stvEmotes = EmoteSet(stv)
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

    fun requestUserEmotes(userId: Int) {}

    fun fetchEmotes() {
        if (global.size == 0) {
            fetchGlobalEmotes()
        }
    }

    companion object {
        private fun createEmptySet(): EmoteContainer {
            return EmoteContainer()
        }
    }
}
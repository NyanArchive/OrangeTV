package tv.orange.features.emotes.component

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.api.component.repository.BttvRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.emotes.component.model.EmoteContainer
import tv.orange.features.emotes.component.model.EmoteContainerCache
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet
import javax.inject.Inject

class EmoteProvider @Inject constructor(
    val bttvRepository: BttvRepository,
    val stvRepository: StvRepository
) {
    private var global = EmoteContainer()
    private val channelEmotesCache = EmoteContainerCache(3)

    private val disposables = CompositeDisposable()

    private fun fetchGlobalEmotes() {
        disposables.add(Single.zip(
            bttvRepository.getBttvGlobalEmotes(),
            bttvRepository.getFfzGlobalEmotes(),
            stvRepository.getStvGlobalEmotes()
        ) { bttv, ffz, stv ->
            val set = EmoteContainer(
                bttvEmotes = EmoteSet(bttv),
                ffzEmotes = EmoteSet(ffz),
                stvEmotes = EmoteSet(stv)
            )

            Logger.debug("New global set: $set")
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
                bttvRepository.getBttvChannelEmotes(channelId).subscribeOn(Schedulers.io()),
                bttvRepository.getFfzChannelEmotes(channelId).subscribeOn(Schedulers.io()),
                stvRepository.getStvChannelEmotes(channelId).subscribeOn(Schedulers.io())
            ) { bttv, ffz, stv ->
                val set = EmoteContainer(
                    bttvEmotes = EmoteSet(bttv),
                    ffzEmotes = EmoteSet(ffz),
                    stvEmotes = EmoteSet(stv)
                )

                Logger.debug("New channel set: $set")
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
        if (global.isEmpty()) {
            fetchGlobalEmotes()
        }
    }

    companion object {
        private fun createEmptySet(): EmoteContainer {
            return EmoteContainer()
        }
    }
}
package tv.orange.features.emotes.component.model.factory

import tv.orange.features.api.component.repository.BttvRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.emotes.component.model.EmotePackageImpl
import tv.orange.features.emotes.component.model.room.Room
import tv.orange.features.emotes.component.model.room.RoomImpl
import javax.inject.Inject

class RoomFactoryImpl @Inject constructor(
    val bttv: BttvRepository,
    val stv: StvRepository
) : RoomFactory {

    override fun create(channelId: Int): Room {
        return RoomImpl().apply {
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { bttv.getBttvChannelEmotes(channelId) }))
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { bttv.getFfzChannelEmotes(channelId) }))
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { stv.getStvChannelEmotes(channelId) }))
        }
    }

    override fun createGlobal(): Room {
        return RoomImpl().apply {
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { bttv.getBttvGlobalEmotes() }))
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { bttv.getFfzGlobalEmotes() }))
            add(EmotePackageImpl(EmoteFetcherFactoryImpl { stv.getStvGlobalEmotes() }))
        }
    }
}
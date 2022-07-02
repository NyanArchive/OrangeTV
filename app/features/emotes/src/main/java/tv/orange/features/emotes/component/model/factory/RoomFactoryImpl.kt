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
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { bttv.getBttvChannelEmotes(channelId) },
                        "BTTV-CHANNEL",
                        channelId
                    )
                )
            )
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { bttv.getFfzChannelEmotes(channelId) },
                        "FFZ-CHANNEL",
                        channelId
                    )
                )
            )
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { stv.getStvChannelEmotes(channelId) },
                        "STV-CHANNEL",
                        channelId
                    )
                )
            )
        }
    }

    override fun createGlobal(): Room {
        return RoomImpl().apply {
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { bttv.getBttvGlobalEmotes() },
                        "BTTV-GLOBAL",
                        0
                    )
                )
            )
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { bttv.getFfzGlobalEmotes() },
                        "FFZ-GLOBAL",
                        0
                    )
                )
            )
            add(
                EmotePackageImpl(
                    EmoteFetcherFactoryImpl(
                        { stv.getStvGlobalEmotes() },
                        "STV-GLOBAL",
                        0
                    )
                )
            )
        }
    }
}
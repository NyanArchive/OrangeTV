package tv.orange.features.emotes.component.model.factory

import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.features.api.component.repository.BttvRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.emotes.component.model.EmotePackageImpl
import tv.orange.features.emotes.component.model.room.Room
import tv.orange.features.emotes.component.model.room.RoomImpl
import tv.orange.models.abs.EmotePackageSet
import javax.inject.Inject

class RoomFactoryImpl @Inject constructor(
    val bttv: BttvRepository,
    val stv: StvRepository
) : RoomFactory {

    override fun create(channelId: Int): Room {
        return RoomImpl().apply {
            if (Flag.BTTV_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { bttv.getBttvChannelEmotes(channelId) },
                        EmotePackageSet.BttvChannel
                    )
                )
            }
            if (Flag.FFZ_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { bttv.getFfzChannelEmotes(channelId) },
                        EmotePackageSet.FfzChannel
                    )
                )
            }
            if (Flag.STV_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { stv.getStvChannelEmotes(channelId) },
                        EmotePackageSet.StvChannel
                    )
                )
            }
        }
    }

    override fun createGlobal(): Room {
        return RoomImpl().apply {
            if (Flag.BTTV_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { bttv.getBttvGlobalEmotes() },
                        EmotePackageSet.BttvGlobal
                    )
                )
            }
            if (Flag.FFZ_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { bttv.getFfzGlobalEmotes() },
                        EmotePackageSet.FfzGlobal
                    )
                )
            }
            if (Flag.STV_EMOTES.valueBoolean()) {
                add(
                    EmotePackageImpl(
                        EmoteFetcherFactoryImpl { stv.getStvGlobalEmotes() },
                        EmotePackageSet.StvGlobal
                    )
                )
            }
        }
    }
}
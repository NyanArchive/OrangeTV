package tv.orange.features.emotes.component.model.factory

import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.api.component.repository.BttvRepository
import tv.orange.features.api.component.repository.ItzRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.emotes.component.model.EmotePackageImpl
import tv.orange.features.emotes.component.model.room.Room
import tv.orange.features.emotes.component.model.room.RoomImpl
import tv.orange.models.abc.EmotePackageSet
import javax.inject.Inject

class RoomFactoryImpl @Inject constructor(
    val bttv: BttvRepository,
    val stv: StvRepository,
    val itz: ItzRepository
) : RoomFactory {

    override fun create(channelId: Int): Room {
        return RoomImpl().apply {
            if (Flag.BTTV_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { bttv.getBttvChannelEmotes(channelId) },
                        token = EmotePackageSet.BttvChannel
                    )
                )
            }
            if (Flag.FFZ_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { bttv.getFfzChannelEmotes(channelId) },
                        token = EmotePackageSet.FfzChannel
                    )
                )
            }
            if (Flag.STV_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { stv.getStvChannelEmotes(channelId) },
                        token = EmotePackageSet.StvChannel
                    )
                )
            }
            if (Flag.ITZ_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { itz.getChannelEmotes(channelId) },
                        token = EmotePackageSet.ItzChannel
                    )
                )
            }
        }
    }

    override fun createGlobal(): Room {
        return RoomImpl().apply {
            if (Flag.BTTV_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { bttv.getBttvGlobalEmotes() },
                        token = EmotePackageSet.BttvGlobal
                    )
                )
            }
            if (Flag.FFZ_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { bttv.getFfzGlobalEmotes() },
                        token = EmotePackageSet.FfzGlobal
                    )
                )
            }
            if (Flag.STV_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { stv.getStvGlobalEmotes() },
                        token = EmotePackageSet.StvGlobal
                    )
                )
            }
            if (Flag.ITZ_EMOTES.asBoolean()) {
                add(
                    EmotePackageImpl(
                        source = EmoteFetcherFactoryImpl { itz.getGlobalEmotes() },
                        token = EmotePackageSet.ItzGlobal
                    )
                )
            }
        }
    }
}
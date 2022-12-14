package tv.orange.features.badges.component.model.factory

import io.reactivex.Single
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.api.component.repository.ChatterinoRepository
import tv.orange.features.api.component.repository.FfzRepository
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.badges.component.model.BadgePackageItzImpl
import tv.orange.features.badges.component.model.BadgePackageImpl
import tv.orange.features.badges.component.model.room.Room
import tv.orange.features.badges.component.model.room.RoomImpl
import tv.orange.models.abc.BadgePackageSet
import tv.orange.models.data.SimpleFetcher
import tv.orange.models.data.badges.impl.BadgeItzSet
import javax.inject.Inject

class RoomFactoryImpl @Inject constructor(
    val ffz: FfzRepository,
    val stv: StvRepository,
    val chatterino: ChatterinoRepository,
    val nop: NopRepository
) : RoomFactory {
    override fun create(): Room {
        return RoomImpl().apply {
            if (Flag.FFZ_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        source = BadgeFetcherFactoryImpl { ffz.getFfzBadges() },
                        token = BadgePackageSet.Ffz
                    )
                )
            }
            if (Flag.STV_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        source = BadgeFetcherFactoryImpl { stv.getStvBadges() },
                        token = BadgePackageSet.Stv
                    )
                )
            }
            if (Flag.CHA_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        source = BadgeFetcherFactoryImpl { chatterino.getChatterinoBadges() },
                        token = BadgePackageSet.Chatterino
                    )
                )
            }
            if (Flag.CHE_BADGES.asBoolean()) {
                add(
                    BadgePackageItzImpl(
                        source = object : SimpleFetcher.SourceFactory<BadgeItzSet> {
                            override fun create(): Single<BadgeItzSet> {
                                return nop.getHomiesBadges()
                            }
                        },
                        token = BadgePackageSet.Homies
                    )
                )
            }
            add(
                BadgePackageImpl(
                    source = BadgeFetcherFactoryImpl { nop.getTwitchModBadges() },
                    token = BadgePackageSet.TwitchMod
                )
            )
        }
    }
}
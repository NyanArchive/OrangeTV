package tv.orange.features.badges.component.model.factory

import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.features.api.component.repository.ChatterinoRepository
import tv.orange.features.api.component.repository.FfzRepository
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.badges.component.model.BadgePackageImpl
import tv.orange.features.badges.component.model.room.Room
import tv.orange.features.badges.component.model.room.RoomImpl
import tv.orange.models.abc.BadgePackageSet
import javax.inject.Inject

class RoomFactoryImpl @Inject constructor(
    val ffz: FfzRepository,
    val stv: StvRepository,
    val chatterino: ChatterinoRepository,
    val nop: NopRepository
) :
    RoomFactory {
    override fun create(): Room {
        return RoomImpl().apply {
            if (Flag.FFZ_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl { ffz.getFfzBadges() },
                        BadgePackageSet.Ffz
                    )
                )
            }
            if (Flag.STV_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl { stv.getStvBadges() },
                        BadgePackageSet.Stv
                    )
                )
            }
            if (Flag.CHA_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl { chatterino.getChatterinoBadges() },
                        BadgePackageSet.Chatterino
                    )
                )
            }
            if (Flag.CHE_BADGES.asBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl { nop.getHomiesBadges() },
                        BadgePackageSet.Homies
                    )
                )
            }
            add(
                BadgePackageImpl(
                    BadgeFetcherFactoryImpl { nop.getTwitchModBadges() },
                    BadgePackageSet.TwitchMod
                )
            )
        }
    }
}
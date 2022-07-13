package tv.orange.features.badges.component.model.factory

import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.features.api.component.repository.ChatterinoRepository
import tv.orange.features.api.component.repository.FfzRepository
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.badges.component.model.BadgePackageImpl
import tv.orange.features.badges.component.model.room.Room
import tv.orange.features.badges.component.model.room.RoomImpl
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
            if (Flag.FFZ_BADGES.valueBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl(
                            { ffz.getFfzBadges() },
                            "FFZ-BADGES"
                        )
                    )
                )
            }
            if (Flag.STV_BADGES.valueBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl(
                            { stv.getStvBadges() },
                            "STV-BADGES"
                        )
                    )
                )
            }
            if (Flag.CHA_BADGES.valueBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl(
                            { chatterino.getChatterinoBadges() },
                            "CHATTERINO-BADGES"
                        )
                    )
                )
            }
            if (Flag.CHE_BADGES.valueBoolean()) {
                add(
                    BadgePackageImpl(
                        BadgeFetcherFactoryImpl(
                            { nop.getHomiesBadges() },
                            "HOMIES-BADGES"
                        )
                    )
                )
            }
            add(
                BadgePackageImpl(
                    BadgeFetcherFactoryImpl(
                        { nop.getTwitchModBadges() },
                        "TWITCHMOD-BADGES"
                    )
                )
            )
        }
    }
}
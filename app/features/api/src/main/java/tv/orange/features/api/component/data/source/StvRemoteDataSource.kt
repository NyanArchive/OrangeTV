package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.api.component.data.api.StvApi
import tv.orange.features.api.component.data.mapper.StvApiMapper
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

class StvRemoteDataSource @Inject constructor(
    val stvApi: StvApi,
    val stvMapper: StvApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return stvApi.globalEmotes().subscribeOn(Schedulers.io()).map { emotes ->
            stvMapper.mapEmotes(emotes, false)
        }
    }

    fun getChannelEmotes(channelId: Int): Single<List<Emote>> {
        return stvApi.emotes(channelId).subscribeOn(Schedulers.io()).map { emotes ->
            stvMapper.mapEmotes(emotes, true)
        }.onErrorResumeNext {
            Logger.debug("Cannot fetch emotes for channel: $channelId")
            Single.just(stvMapper.mapEmotes(emptyList(), true))
        }
    }

    fun getAvatars(): Single<AvatarSet> {
        return stvApi.avatars().subscribeOn(Schedulers.io()).map { map ->
            stvMapper.mapAvatars(map)
        }
    }

    fun getBadges(): Single<BadgeSet> {
        return stvApi.badges().subscribeOn(Schedulers.io()).map { map ->
            stvMapper.mapBadges(map)
        }
    }
}
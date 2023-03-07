package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.core.LoggerImpl
import tv.orange.features.api.component.data.api.StvApi
import tv.orange.features.api.component.data.api.StvOldApi
import tv.orange.features.api.component.data.mapper.StvApiMapper
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

class StvRemoteDataSource @Inject constructor(
    val stvApi: StvApi,
    val stvOldApi: StvOldApi,
    val stvMapper: StvApiMapper
) {
    fun getGlobalEmotes(): Single<List<Emote>> {
        return stvApi.globalEmotes().subscribeOn(Schedulers.io()).map { globalSet ->
            stvMapper.mapEmotes(set = globalSet, isChannelEmotes = false)
        }
    }

    fun getChannelEmotes(channelId: Int): Single<List<Emote>> {
        return stvApi.emotes(channelId).subscribeOn(Schedulers.io()).map { user ->
            stvMapper.mapEmotes(set = user.emote_set, isChannelEmotes = true)
        }.onErrorResumeNext {
            LoggerImpl.warning("Cannot fetch emotes for channel: $channelId")
            Single.just(emptyList())
        }
    }

    fun getAvatars(): Single<AvatarSet> {
        return stvApi.avatars().subscribeOn(Schedulers.io()).map(stvMapper::map)
    }

    fun getBadges(): Single<BadgeSet> {
        return stvOldApi.badges().subscribeOn(Schedulers.io()).map(stvMapper::map)
    }
}
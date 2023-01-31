package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.ItzRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.badges.BadgeSet
import tv.orange.models.data.emotes.EmoteSet
import javax.inject.Inject

@ApiScope
class ItzRepository @Inject constructor(
    val itzRemoteDataSource: ItzRemoteDataSource
) {
    fun getGlobalEmotes(): Single<EmoteSet> {
        return itzRemoteDataSource.getGlobalEmotes().map { EmoteSet(it) }
    }

    fun getChannelEmotes(channelId: Int): Single<EmoteSet> {
        return itzRemoteDataSource.getChannelEmotes(channelId = channelId).map { EmoteSet(it) }
    }

    fun getGlobalBadges1(): Single<BadgeSet> {
        return itzRemoteDataSource.getGlobalBadges1()
    }

    fun getGlobalBadges2(): Single<BadgeSet> {
        return itzRemoteDataSource.getGlobalBadges2()
    }
}
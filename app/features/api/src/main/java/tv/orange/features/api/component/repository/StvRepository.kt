package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.StvRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.emotes.Emote
import javax.inject.Inject

@ApiScope
class StvRepository @Inject constructor(
    val stvDataSource: StvRemoteDataSource
) {
    fun getStvGlobalEmotes(): Single<List<Emote>> {
        return stvDataSource.getGlobalEmotes()
    }

    fun getStvChannelEmotes(channelId: Int): Single<List<Emote>> {
        return stvDataSource.getChannelEmotes(channelId)
    }

    fun getStvAvatars(): Single<AvatarSet> {
        return stvDataSource.getAvatars()
    }
}
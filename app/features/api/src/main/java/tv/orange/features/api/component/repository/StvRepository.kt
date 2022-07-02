package tv.orange.features.api.component.repository

import io.reactivex.Single
import tv.orange.features.api.component.data.source.StvRemoteDataSource
import tv.orange.features.api.di.scope.ApiScope
import tv.orange.models.data.avatars.AvatarSet
import tv.orange.models.data.emotes.Emote
import tv.orange.models.data.emotes.EmoteSet
import javax.inject.Inject

@ApiScope
class StvRepository @Inject constructor(
    val stvDataSource: StvRemoteDataSource
) {
    fun getStvGlobalEmotes(): Single<EmoteSet> {
        return stvDataSource.getGlobalEmotes().map { EmoteSet(it) }
    }

    fun getStvChannelEmotes(channelId: Int): Single<EmoteSet> {
        return stvDataSource.getChannelEmotes(channelId).map { EmoteSet(it) }
    }

    fun getStvAvatars(): Single<AvatarSet> {
        return stvDataSource.getAvatars()
    }
}
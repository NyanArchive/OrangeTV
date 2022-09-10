package tv.orange.features.chapters.component.data.source

import com.apollographql.apollo3.api.Optional
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.chapters.component.data.mapper.ChaptersMapper
import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.models.abc.TwitchComponentProvider
import tv.orange.models.gql.twitch.ChapterSelectButtonQuery
import tv.twitch.android.network.graphql.GraphQlService
import javax.inject.Inject

class ChaptersDataSource @Inject constructor(
    val tcp: TwitchComponentProvider,
    val mapper: ChaptersMapper
) {
    fun getChapters(vodId: String): Single<List<Chapter>> {
        return tcp.getProvider(GraphQlService::class).get().singleForQuery(
            ChapterSelectButtonQuery(videoID = Optional.presentIfNotNull(vodId)),
            mapper::map, true, true, true, false
        ).subscribeOn(Schedulers.io())
    }
}
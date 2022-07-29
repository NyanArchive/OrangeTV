package tv.orange.features.chapters.component.data.mapper

import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.models.gql.twitch.VideoPlayer_ChapterSelectButtonVideoQuery
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChaptersMapper @Inject constructor() {
    fun map(data: VideoPlayer_ChapterSelectButtonVideoQuery.Data): List<Chapter> {
        return data.video?.moments?.edges?.map {
            it.node.let { node ->
                Chapter(
                    title = node.description ?: "Unknown",
                    timestamp = TimeUnit.MILLISECONDS.toSeconds(node.positionMilliseconds.toLong()).toInt(),
                    url = node.details?.onGameChangeMomentDetails?.game?.boxArtURL
                )
            }
        } ?: emptyList()
    }
}
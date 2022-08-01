package tv.orange.features.chapters.component.data.mapper

import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.models.gql.twitch.VideoPlayer_ChapterSelectButtonVideoQuery
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChaptersMapper @Inject constructor() {
    fun map(data: VideoPlayer_ChapterSelectButtonVideoQuery.Data): List<Chapter> {
        val res = data.video?.moments?.edges?.map {
            it.node.let { node ->
                Chapter(
                    title = node.description ?: "Unknown",
                    timestamp = TimeUnit.MILLISECONDS.toSeconds(node.positionMilliseconds.toLong())
                        .toInt(),
                    url = node.details?.onGameChangeMomentDetails?.game?.boxArtURL
                )
            }
        }
        if (res.isNullOrEmpty()) {
            return listOf(
                Chapter(
                    title = data.video?.game?.displayName ?: "Unknown",
                    timestamp = 0,
                    url = data.video?.game?.boxArtURL
                )
            )
        }
        return res
    }
}
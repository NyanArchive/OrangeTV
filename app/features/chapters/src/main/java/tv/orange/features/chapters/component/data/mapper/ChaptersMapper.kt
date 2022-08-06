package tv.orange.features.chapters.component.data.mapper

import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.models.gql.twitch.ChapterSelectButtonQuery
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChaptersMapper @Inject constructor() {
    fun map(data: ChapterSelectButtonQuery.Data): List<Chapter> {
        return data.video?.moments?.edges?.map { edge ->
            val node = edge.node
            Chapter(
                title = node.description ?: "Unknown",
                timestamp = TimeUnit.MILLISECONDS.toSeconds(node.positionMilliseconds.toLong())
                    .toInt(),
                url = node.details?.onGameChangeMomentDetails?.game?.boxArtURL
            )
        }.takeIf { !it.isNullOrEmpty() } ?: listOf(
            Chapter(
                title = data.video?.game?.displayName ?: "Unknown",
                timestamp = 0,
                url = data.video?.game?.boxArtURL
            )
        )
    }
}
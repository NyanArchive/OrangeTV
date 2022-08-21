package tv.orange.features.chapters.component.data.mapper

import tv.orange.core.ResourceManager
import tv.orange.features.chapters.component.data.model.Chapter
import tv.orange.models.gql.twitch.ChapterSelectButtonQuery
import tv.orange.models.util.DateUtil
import javax.inject.Inject

class ChaptersMapper @Inject constructor(val resourceManager: ResourceManager) {
    fun map(response: ChapterSelectButtonQuery.Data): List<Chapter> {
        return response.video?.moments?.edges?.map { edge ->
            edge.node.let { node ->
                Chapter(
                    title = node.description ?: resourceManager.getString(
                        resName = "orange_chapters_unknown"
                    ),
                    timestamp = DateUtil.toSeconds(node.positionMilliseconds.toLong()).toInt(),
                    url = node.details?.onGameChangeMomentDetails?.game?.boxArtURL
                        ?: response.video?.game?.boxArtURL
                )
            }
        }.takeIf { !it.isNullOrEmpty() } ?: listOf(
            Chapter(
                title = response.video?.game?.displayName ?: resourceManager.getString(
                    resName = "orange_chapters_unknown"
                ),
                timestamp = 0,
                url = response.video?.game?.boxArtURL
            )
        )
    }
}
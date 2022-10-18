package tv.orange.features.highlighter.data.mapper

import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.db.entities.KeywordEntity
import javax.inject.Inject

class HighlighterMapper @Inject constructor() {
    fun map(entities: List<KeywordEntity>): List<KeywordData> {
        return entities.map { entity ->
            map(entity)
        }
    }

    fun map(entity: KeywordEntity): KeywordData {
        return KeywordData(
            word = entity.word,
            type = KeywordData.Type.valueOf(entity.type),
            color = entity.color,
            vibration = entity.vibration
        )
    }

    fun map(keyword: KeywordData): KeywordEntity {
        return KeywordEntity(
            word = keyword.word,
            type = keyword.type.name,
            color = keyword.color,
            vibration = keyword.vibration
        )
    }

    fun map(rawText: String): List<KeywordData> {
        return rawText.split("\\s+".toRegex())
            .filter { it.isNotBlank() }.map { it.trim() }.mapNotNull { word ->
                when (word[0]) {
                    '@' -> {
                        if (word.length < 2) {
                            return@mapNotNull null
                        }
                        KeywordData(
                            word = word.substring(1).lowercase(),
                            type = KeywordData.Type.USERNAME,
                            color = KeywordData.DEFAULT_COLOR
                        )
                    }
                    '#' -> {
                        if (word.length < 2) {
                            return@mapNotNull null
                        }
                        KeywordData(
                            word = word.substring(1).lowercase(),
                            type = KeywordData.Type.INSENSITIVE,
                            color = KeywordData.DEFAULT_COLOR
                        )
                    }
                    else -> {
                        KeywordData(
                            word = word,
                            type = KeywordData.Type.CASESENSITIVE,
                            color = KeywordData.DEFAULT_COLOR
                        )
                    }
                }
            }
    }
}
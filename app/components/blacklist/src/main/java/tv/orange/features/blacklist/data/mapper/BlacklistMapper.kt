package tv.orange.features.blacklist.data.mapper

import tv.orange.features.blacklist.data.model.KeywordData
import tv.orange.features.blacklist.db.entities.KeywordEntity
import javax.inject.Inject

class BlacklistMapper @Inject constructor() {
    fun map(entities: List<KeywordEntity>): List<KeywordData> {
        return entities.map { entity ->
            map(entity)
        }
    }

    fun map(entity: KeywordEntity): KeywordData {
        return KeywordData(
            word = entity.word,
            type = KeywordData.Type.valueOf(entity.type)
        )
    }

    fun map(keyword: KeywordData): KeywordEntity {
        return KeywordEntity(
            word = keyword.word,
            type = keyword.type.name
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
                            type = KeywordData.Type.USERNAME
                        )
                    }
                    '#' -> {
                        if (word.length < 2) {
                            return@mapNotNull null
                        }
                        KeywordData(
                            word = word.substring(1).lowercase(),
                            type = KeywordData.Type.INSENSITIVE
                        )
                    }
                    else -> {
                        KeywordData(
                            word = word,
                            type = KeywordData.Type.CASESENSITIVE
                        )
                    }
                }
            }
    }
}
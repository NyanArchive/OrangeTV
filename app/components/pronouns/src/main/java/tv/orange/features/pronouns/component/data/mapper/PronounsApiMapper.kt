package tv.orange.features.pronouns.component.data.mapper

import io.reactivex.Single
import tv.orange.models.data.UserPronoun
import tv.orange.models.retrofit.alejo.UserPronounData
import javax.inject.Inject

class PronounsApiMapper @Inject constructor() {
    fun mapPronouns(
        userName: String,
        response: Single<List<UserPronounData>>
    ): Single<UserPronoun> {
        return response.map {
            if (it.isEmpty()) {
                return@map UserPronoun(userName = userName)
            }

            return@map UserPronoun(userName = userName, pronoun_id = it[0].pronoun_id)
        }
    }
}
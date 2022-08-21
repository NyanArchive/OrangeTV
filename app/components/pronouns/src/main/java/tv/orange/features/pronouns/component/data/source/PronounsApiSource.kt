package tv.orange.features.pronouns.component.data.source

import io.reactivex.Single
import tv.orange.features.pronouns.component.data.api.PronounsApi
import tv.orange.features.pronouns.component.data.mapper.PronounsApiMapper
import tv.orange.features.pronouns.di.scope.PronounScope
import tv.orange.models.data.UserPronoun
import tv.orange.models.retrofit.alejo.PronounItemData
import javax.inject.Inject

@PronounScope
class PronounsApiSource @Inject constructor(
    val pronounsApi: PronounsApi,
    val pronounsApiMapper: PronounsApiMapper
) {
    fun getPronouns(): Single<List<PronounItemData>> {
        return pronounsApi.pronouns()
    }

    fun getUserPronoun(userName: String): Single<UserPronoun> {
        return pronounsApiMapper.mapPronouns(
            userName = userName,
            response = pronounsApi.getUserPronoun(userName)
        )
    }
}
package tv.orange.features.pronouns.component.data.repository

import io.reactivex.Single
import tv.orange.features.pronouns.component.data.source.PronounsApiSource
import tv.orange.features.pronouns.di.scope.PronounScope
import tv.orange.models.data.UserPronoun
import tv.orange.models.retrofit.alejo.PronounItemData
import javax.inject.Inject

@PronounScope
class PronounsRepository @Inject constructor(
    val pronounsApiSource: PronounsApiSource
): IRepository {
    override fun getPronouns(): Single<List<PronounItemData>> {
        return pronounsApiSource.getPronouns()
    }

    override fun getUserPronoun(userName: String): Single<UserPronoun> {
        return pronounsApiSource.getUserPronoun(userName)
    }
}
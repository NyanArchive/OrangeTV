package tv.orange.features.pronouns.component.data.repository

import io.reactivex.Single
import tv.orange.models.data.UserPronoun
import tv.orange.models.retrofit.alejo.PronounItemData

interface IRepository {
    fun getPronouns(): Single<List<PronounItemData>>
    fun getUserPronoun(userName: String): Single<UserPronoun>
}
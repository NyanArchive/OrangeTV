package tv.orange.features.api.component.data.source

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tv.orange.features.api.component.data.api.FFZAPApi
import tv.orange.features.api.component.data.mapper.FfzApiMapper
import tv.orange.models.data.badges.BadgeSet
import javax.inject.Inject

class FfzAPRemoteDataSource @Inject constructor(
    val ffzapApi: FFZAPApi,
    val ffzMapper: FfzApiMapper
) {
    fun getBadges(): Single<BadgeSet> {
        return ffzapApi.supporters().subscribeOn(Schedulers.io()).map(ffzMapper::mapBadges)
    }
}
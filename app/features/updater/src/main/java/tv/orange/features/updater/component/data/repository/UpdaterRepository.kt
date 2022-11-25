package tv.orange.features.updater.component.data.repository

import io.reactivex.Single
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.UpdateChannel
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.updater.component.data.mapper.UpdaterDataMapper
import tv.orange.features.updater.component.data.model.UpdateData
import tv.twitch.android.util.Optional
import javax.inject.Inject

class UpdaterRepository @Inject constructor(
    val nopRepository: NopRepository,
    val mapper: UpdaterDataMapper
) {
    fun observeUpdate(): Single<Optional<UpdateData>> {
        return nopRepository.getUpdateData()
            .map { data ->
                when (Flag.UPDATER.asVariant<UpdateChannel>()) {
                    UpdateChannel.Release -> data.release
                    UpdateChannel.Beta -> data.beta
                    UpdateChannel.Dev -> data.dev
                    else -> null
                }?.let { channelData ->
                    Optional.of(channelData)
                } ?: Optional.empty()
            }.map {
                if (it.isPresent) {
                    val data = it.get()
                    if (!data.active) {
                        Optional.empty()
                    } else {
                        Optional.of(mapper.map(it.get()))
                    }
                } else {
                    Optional.empty()
                }
            }
    }
}
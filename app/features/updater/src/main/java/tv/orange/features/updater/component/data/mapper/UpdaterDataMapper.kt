package tv.orange.features.updater.component.data.mapper

import tv.orange.features.updater.component.data.model.UpdateData
import tv.orange.models.retrofit.nop.UpdateChannelData
import javax.inject.Inject

class UpdaterDataMapper @Inject constructor() {
    fun map(data: UpdateChannelData?): UpdateData? {
        data ?: return null

        return UpdateData(
            build = data.build ?: -1,
            codename = data.codename ?: "Unknown",
            url = data.apkUrl!![0],
            logoUrl = data.logoUrl,
            changelog = data.changelog
        )
    }
}
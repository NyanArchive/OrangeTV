package tv.orange.features.tracking

import android.content.Context
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.models.abc.Feature
import tv.orange.models.retrofit.nop.PinnyInfo
import tv.twitch.android.util.UniqueDeviceIdentifier
import javax.inject.Inject

class Tracking @Inject constructor(
    val context: Context,
    val nopRepository: NopRepository
) : Feature {
    private val disposables = CompositeDisposable()

    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Tracking::class.java)
    }

    private fun ping() {
        disposables.add(
            nopRepository.ping(
                buildNumber = BuildConfigUtil.buildConfig.number,
                deviceId = UniqueDeviceIdentifier.getInstance().getUniqueID(context)
            ).subscribe({
                LoggerImpl.info("OK")
            }, Throwable::printStackTrace)
        )
    }

    fun initialize() {
        ping()
    }

    fun getPinnyInfo(): Single<PinnyInfo> {
        return nopRepository.pingInfo(BuildConfigUtil.buildConfig.number)
    }

    override fun onCreateFeature() {}
}
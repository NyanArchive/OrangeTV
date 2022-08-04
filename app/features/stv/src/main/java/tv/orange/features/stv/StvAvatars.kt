package tv.orange.features.stv

import io.reactivex.Single
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.LoggerWithTag
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.core.models.FlagListener
import tv.orange.core.models.LifecycleAware
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.stv.di.scope.StvScope
import tv.orange.models.Feature
import tv.orange.models.SimpleFetcher
import tv.orange.models.data.avatars.AvatarSet
import javax.inject.Inject

@StvScope
class StvAvatars @Inject constructor(
    val stvRepository: StvRepository
) : SimpleFetcher<AvatarSet>(
    dataSourceFactory = object : SourceFactory<AvatarSet> {
        override fun create(): Single<AvatarSet> {
            return stvRepository.getStvAvatars()
        }
    },
    logger = LoggerWithTag("7TV-Avatars")
), LifecycleAware, FlagListener, Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(StvAvatars::class.java)
    }

    fun hookProfileImageUrl(profileImageUrl: String, channelName: String): String {
        Logger.debug("profileImageUrl: $profileImageUrl, channelName: $channelName")

        getData()?.let { set ->
            if (set.isEmpty()) {
                return profileImageUrl
            }

            return set.get(channelName) ?: profileImageUrl
        }

        return profileImageUrl
    }

    override fun onAllComponentDestroyed() {
        Logger.debug("called")
        clear()
    }

    override fun onFirstActivityCreated() {
        if (!Flag.STV_AVATARS.valueBoolean()) {
            return
        }

        refresh(force = true)
    }

    override fun onFlagChanged(flag: Flag) {
        if (flag == Flag.STV_AVATARS) {
            onAllComponentDestroyed()
            onFirstActivityCreated()
        }
    }

    override fun onAllComponentStopped() {}
    override fun onSdkResume() {}
    override fun onAccountLogout() {}
    override fun onFirstActivityStarted() {}
    override fun onConnectedToChannel(channelId: Int) {}
    override fun onConnectingToChannel(channelId: Int) {}

    override fun onDestroyFeature() = onAllComponentDestroyed()
    override fun onCreateFeature() {}
}
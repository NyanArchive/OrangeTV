package tv.orange.features.stv

import io.reactivex.Single
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.LoggerWithTag
import tv.orange.core.PreferenceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.FlagListener
import tv.orange.core.models.lifecycle.LifecycleAware
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.models.AutoInitialize
import tv.orange.models.abc.Feature
import tv.orange.models.data.SimpleFetcher
import tv.orange.models.data.avatars.AvatarSet
import javax.inject.Inject

@AutoInitialize
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
        getData()?.let { set ->
            if (set.isEmpty()) {
                return profileImageUrl
            }

            return set.get(channelName = channelName) ?: profileImageUrl
        }

        return profileImageUrl
    }

    override fun onAllComponentDestroyed() {
        clear()
    }

    override fun onFirstActivityCreated() {
        if (!Flag.STV_AVATARS.asBoolean()) {
            return
        }

        refresh(force = true)
    }

    override fun onFlagValueChanged(flag: Flag) {
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

    override fun onCreateFeature() {
        Core.get().registerLifecycleListeners(this)
        PreferenceManager.get().registerFlagListeners(this)
    }
}
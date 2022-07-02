package tv.orange.features.stv

import io.reactivex.disposables.CompositeDisposable
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.di.component.CoreComponent
import tv.orange.core.models.LifecycleAware
import tv.orange.features.api.component.repository.StvRepository
import tv.orange.features.api.di.component.ApiComponent
import tv.orange.features.stv.di.component.DaggerStvComponent
import tv.orange.features.stv.di.scope.StvScope
import tv.orange.models.data.avatars.AvatarSet
import javax.inject.Inject

@StvScope
class AvatarsHookProvider @Inject constructor(val stvRepository: StvRepository) : LifecycleAware {
    private var avatarSet: AvatarSet? = null

    private val disposables = CompositeDisposable()

    companion object {
        private val INSTANCE: AvatarsHookProvider by lazy {
            val hook = DaggerStvComponent.builder()
                .coreComponent(Core.getInjector().provideComponent(CoreComponent::class))
                .apiComponent(Core.getInjector().provideComponent(ApiComponent::class))
                .build().provider

            Logger.debug("Provide new instance: $hook")
            return@lazy hook
        }

        @JvmStatic
        fun get(): AvatarsHookProvider {
            return INSTANCE
        }
    }

    fun hookProfileImageUrl(profileImageUrl: String, channelName: String): String {
        return avatarSet?.get(channelName) ?: profileImageUrl
    }

    override fun onAllComponentDestroyed() {
        disposables.clear()
        avatarSet = null
    }

    override fun onAllComponentStopped() {}

    override fun onSdkResume() {}

    override fun onAccountLogout() {}

    override fun onFirstActivityCreated() {
        disposables.add(stvRepository.getStvAvatars().subscribe({
            avatarSet = it
            Logger.debug("Fetched: $it")
        }, {
            it.printStackTrace()
        }))
    }

    override fun onFirstActivityStarted() {}

    override fun onConnectedToChannel(channelId: Int) {}

    override fun onConnectingToChannel(channelId: Int) {}
}
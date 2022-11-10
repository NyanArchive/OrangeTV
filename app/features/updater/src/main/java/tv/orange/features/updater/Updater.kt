package tv.orange.features.updater

import android.content.Context
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.BuildConfigUtil
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.UpdateChannel
import tv.orange.features.api.component.repository.NopRepository
import tv.orange.features.updater.data.view.UpdaterActivity
import tv.orange.models.abc.Feature
import tv.orange.models.retrofit.nop.UpdateChannelData
import javax.inject.Inject

class Updater @Inject constructor(val nopRepository: NopRepository) : Feature {
    private val disposables = CompositeDisposable()

    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Updater::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(Updater::class.java)
        }
    }

    private fun checkUpdates(context: Context, variant: UpdateChannel, silent: Boolean = true) {
        disposables.add(
            nopRepository.getUpdateData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ data ->
                    when (variant) {
                        UpdateChannel.Disabled -> null
                        UpdateChannel.Release -> data.release
                        UpdateChannel.Beta -> data.beta
                        UpdateChannel.Dev -> data.dev
                    }?.let { channel ->
                        tryInstall(context = context, channel = channel, silent = silent)
                    }
                }, { th ->
                    th.printStackTrace()
                    Toast.makeText(
                        context,
                        "Updater::Error::${th.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        )
    }

    private fun tryInstall(context: Context, channel: UpdateChannelData, silent: Boolean = true) {
        if (!channel.active) {
            Logger.debug("DISABLED")
            if (!silent) {
                Core.toast("Updates disabled")
            }
            return
        }
        if ((channel.build ?: 0) <= BuildConfigUtil.buildConfig.number) {
            Logger.debug("BUILD")
            if (!silent) {
                Core.toast("You are using the latest version")
            }
            return
        }

        UpdaterActivity.startActivity(
            context = context,
            codename = channel.codename,
            url = channel.apkUrl?.get(0)!!, // FIXME
            logoUrl = channel.logoUrl,
            build = channel.build,
            changelog = channel.changelog
        )
    }

    fun checkUpdates(context: Context, silent: Boolean = true) {
        Logger.debug("called")
        when (val variant = Flag.UPDATER.asVariant<UpdateChannel>()) {
            UpdateChannel.Disabled -> {
                Logger.debug("DISABLED")
            }
            else -> {
                checkUpdates(context = context, variant = variant, silent = silent)
            }
        }
    }

    fun createUpdaterFragment(context: Context) {
        checkUpdates(context)
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
package tv.orange.features.vodsync

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.vodsync.di.scope.VodSyncScope
import tv.orange.features.vodsync.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate
import tv.twitch.android.feature.theatre.common.StreamSettings
import tv.twitch.android.models.videos.VodModel
import javax.inject.Inject

@VodSyncScope
class VodSync @Inject constructor(
    val viewFactory: ViewFactory
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(VodSync::class.java)
    }

    fun hookChommentTimestamp(
        vodModel: VodModel,
        timestamp: Int
    ): Int {
        val progress = PreferenceManager.get().getChommentSeekerValue(vodModel.id)

        if (progress == 0) {
            return timestamp
        }

        return timestamp + progress;
    }

    fun renderChommentSeekerSection(
        section: ViewGroup,
        header: View,
        player: StreamSettings.ConfigurablePlayer
    ) {
        val seekbar = section.getView<SeekBar>("stream_settings_fragment__chomment_seeker")
        val value = section.getView<TextView>("stream_settings_fragment__chomment_seeker_value")

        val vodId = player.vod ?: run {
            section.visibility = View.GONE
            header.visibility = View.GONE
            seekbar.setOnSeekBarChangeListener(null)
            return
        }

        val currentProgress = PreferenceManager.get().getChommentSeekerValue(vodId.id)
        if (header is TextView) {
            header.text = ResourceManager.get().getString("orange_seeker_header_text")
        }

        section.visibility = View.VISIBLE
        header.visibility = View.VISIBLE

        seekbar.apply {
            max = 180
            progress = currentProgress + 60
        }
        value.apply {
            text = if (currentProgress > 0) {
                "+$currentProgress"
            } else {
                currentProgress.toString()
            }
        }
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val realProgress = progress - 60
                PreferenceManager.get().saveChommentSeekerValue(vodId.id, realProgress)
                value.text = if (realProgress > 0) {
                    "+$realProgress"
                } else {
                    realProgress.toString()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun getChommentSeekerSection(delegate: BaseViewDelegate): ViewGroup {
        return viewFactory.getChommentSeekerSection(delegate)
    }

    fun getChommentSeekerHeader(delegate: BaseViewDelegate): View {
        return viewFactory.getChommentSeekerHeader(delegate)
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
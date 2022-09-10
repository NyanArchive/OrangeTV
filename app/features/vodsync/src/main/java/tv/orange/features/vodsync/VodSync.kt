package tv.orange.features.vodsync

import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.vodsync.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate
import tv.twitch.android.feature.theatre.common.StreamSettings
import tv.twitch.android.models.videos.VodModel
import javax.inject.Inject

class VodSync @Inject constructor(
    val viewFactory: ViewFactory,
    val prefManager: PreferenceManager
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(VodSync::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(VodSync::class.java)
        }

        private fun drawValue(view: TextView, value: Int) {
            view.text = if (value > 0) {
                "+$value"
            } else {
                value.toString()
            }
        }
    }

    fun hookChommentTimestamp(
        vodModel: VodModel,
        timestamp: Int
    ): Int {
        val progress = prefManager.getChommentSeekerValue(videoId = vodModel.id)

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
        val seekbar = section.getView<SeekBar>(
            resName = "stream_settings_fragment__chomment_seeker"
        )
        val value = section.getView<TextView>(
            resName = "stream_settings_fragment__chomment_seeker_value"
        )

        val vodId = player.vod ?: run {
            section.changeVisibility(false)
            header.changeVisibility(false)
            seekbar.setOnSeekBarChangeListener(null)
            return
        }

        val savedSyncValue = prefManager.getChommentSeekerValue(videoId = vodId.id)
        if (header is TextView) {
            header.text = ResourceManager.get().getString(
                resName = "orange_seeker_header_text"
            )
        }

        section.changeVisibility(true)
        header.changeVisibility(true)

        seekbar.apply {
            max = 180
            progress = savedSyncValue + 60
        }
        drawValue(value, savedSyncValue)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val currentSyncValue = progress - 60
                PreferenceManager.get().saveChommentSeekerValue(
                    videoId = vodId.id,
                    value = currentSyncValue
                )
                drawValue(value, currentSyncValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun getChommentSeekerSection(delegate: BaseViewDelegate): ViewGroup {
        return viewFactory.getChommentSeekerSection(delegate = delegate)
    }

    fun getChommentSeekerHeader(delegate: BaseViewDelegate): View {
        return viewFactory.getChommentSeekerHeader(delegate = delegate)
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}
}
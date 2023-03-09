package tv.orange.features.vodspeed.bridge

import tv.twitch.android.models.videos.VodModel
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter

interface IVodSpeedDelegate {
    fun hideVodSpeedButton()

    fun onBindVodModel(vod: VodModel, presenter: SeekbarOverlayPresenter)
}
package tv.orange.features.chapters.bridge

import tv.twitch.android.models.videos.VodModel
import tv.twitch.android.shared.player.overlay.seekable.SeekbarOverlayPresenter

interface IChaptersDelegate {
    fun hideChaptersButton()

    fun onBindVodModel(vod: VodModel, presenter: SeekbarOverlayPresenter)
}
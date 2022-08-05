package tv.orange.features.vodsync.view

import android.view.View
import android.view.ViewGroup
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate

interface ViewFactory {
    fun getChommentSeekerSection(delegate: BaseViewDelegate): ViewGroup
    fun getChommentSeekerHeader(delegate: BaseViewDelegate): View
}
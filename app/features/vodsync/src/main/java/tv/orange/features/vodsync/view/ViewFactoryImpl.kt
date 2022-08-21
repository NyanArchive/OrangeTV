package tv.orange.features.vodsync.view

import android.view.View
import android.view.ViewGroup
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.vodsync.di.scope.VodSyncScope
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate
import javax.inject.Inject

@VodSyncScope
class ViewFactoryImpl @Inject constructor() : ViewFactory {
    override fun getChommentSeekerSection(delegate: BaseViewDelegate): ViewGroup {
        return delegate.contentView.getView(resName = "stream_settings_fragment__chomment_seeker_wrapper")
    }

    override fun getChommentSeekerHeader(delegate: BaseViewDelegate): View {
        return delegate.contentView.getView(resName = "stream_settings_fragment__chomment_seeker_header")
    }
}
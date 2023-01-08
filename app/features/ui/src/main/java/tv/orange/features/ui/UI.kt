package tv.orange.features.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import tv.orange.core.Core
import tv.orange.core.LoggerImpl
import tv.orange.core.PreferenceManagerCore
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.BottomNavbarPosition
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.ui.bridge.SupportBridge
import tv.orange.models.abc.Feature
import tv.twitch.android.shared.chat.ChatViewDelegate
import tv.twitch.android.shared.chat.emotecard.FollowButtonUiModel
import tv.twitch.android.shared.player.overlay.PlayerOverlayViewDelegate
import tv.twitch.android.shared.player.overlay.stream.StreamOverlayConfiguration
import tv.twitch.android.shared.ui.elements.navigation.BottomNavigationDestination
import tv.twitch.android.shared.ui.elements.navigation.BottomNavigationItem
import javax.inject.Inject

class UI @Inject constructor(
    val context: Context,
    val supportBridge: SupportBridge,
    val prefManager: PreferenceManagerCore
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(UI::class.java)

        @JvmStatic
        val hideResumeWatching: Boolean
            get() = Flag.HIDE_RESUME_WATCHING_SECTION.asBoolean()

        @JvmStatic
        val hideRecommendedStreams: Boolean
            get() = Flag.HIDE_RECOMMENDATION_SECTION.asBoolean()

        @JvmStatic
        val hideGames: Boolean
            get() = Flag.HIDE_GAME_SECTION.asBoolean()

        @JvmStatic
        val hideOfflineChannels: Boolean
            get() = Flag.HIDE_OFFLINE_CHANNEL_SECTION.asBoolean()

        @JvmStatic
        val hideLeaderboards: Boolean
            get() = Flag.HIDE_LEADERBOARDS.asBoolean()

        @JvmStatic
        val disableHypeTrain: Boolean
            get() = Flag.DISABLE_HYPE_TRAIN.asBoolean()

        @JvmStatic
        val hideDiscoverTab: Boolean
            get() = Flag.HIDE_DISCOVER_TAB.asBoolean()

        @JvmStatic
        val showFullCards: Boolean
            get() = Flag.FOLLOWED_FULL_CARDS.asBoolean()

        @JvmStatic
        val landscapeContainerScale: Int
            get() = Flag.LANDSCAPE_CHAT_SIZE.asInt()

        @JvmStatic
        val landscapeSplitContainerScale: Int
            get() = Flag.LANDSCAPE_SPLIT_CHAT_SIZE.asInt()

        @JvmStatic
        val forwardSeek: Int
            get() = Flag.FORWARD_SEEK.asInt()

        @JvmStatic
        val rewindSeek: Int
            get() = -Flag.REWIND_SEEK.asInt()

        @JvmStatic
        val miniPlayerSizeScale: Float
            get() = Flag.MINI_PLAYER_SIZE.asInt().div(100F)

        @JvmStatic
        fun hookMiniPlayerWidth(org: Int): Int = (miniPlayerSizeScale * org).toInt()

        @JvmStatic
        val hideChatHeader: Boolean
            get() = Flag.HIDE_TOP_CHAT_PANEL_VODS.asBoolean()

        @JvmStatic
        val isAutoplayDisabled: Boolean
            get() = Flag.DISABLE_THEATRE_AUTOPLAY.asBoolean()

        private fun isLandscapeOrientation(context: Context): Boolean =
            context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        @JvmStatic
        fun shouldHideMessageInput(context: Context): Boolean {
            return Flag.AUTO_HIDE_MESSAGE_INPUT.asBoolean() && isLandscapeOrientation(context = context)
        }

        @JvmStatic
        fun hookPlayerMetadataViewId(org: Int): Int {
            if (!Flag.COMPACT_PLAYER_FOLLOW_VIEW.asBoolean()) {
                return org
            }

            return ResourceManager.get().getLayoutId("orangetv_player_metadata_view_extended")
        }

        @JvmStatic
        fun maybeHideFollowButton(
            view: View?,
            model: FollowButtonUiModel?
        ) {
            view ?: return

            if (Flag.HIDE_UNFOLLOW_BUTTON.asBoolean()) {
                model?.let {
                    if (it.isFollowing) {
                        view.changeVisibility(false)
                    }
                }
            }
        }

        @JvmStatic
        fun getUptimeButton(vh: RecyclerView.ViewHolder): TextView {
            return vh.itemView.getView<TextView>("compact_stream_item__uptime")
        }

        @JvmStatic
        val shouldShowVideoDebugPanel: Boolean
            get() = Flag.SHOW_STATS_BUTTON.asBoolean()

        @JvmStatic
        val showClipfinity: Boolean
            get() = Flag.CLIPFINITY.asBoolean()

        @JvmStatic
        val showFollowButtonExtended: Boolean
            get() = !Flag.HIDE_UNFOLLOW_BUTTON.asBoolean()

        @JvmStatic
        val hideFSB: Boolean
            get() = Flag.HIDE_FSB.asBoolean()

        @JvmStatic
        val forceToolbarSearch: Boolean
            get() = Flag.FORCE_TOOLBAR_SEARCH_BUTTON.asBoolean()
    }

    val skipTwitchBrowserDialog: Boolean
        get() = Flag.DISABLE_LINK_DISCLAIMER.asBoolean()

    fun filterNavItems(items: MutableList<BottomNavigationItem>?): MutableList<BottomNavigationItem>? {
        if (items.isNullOrEmpty()) {
            return items
        }

        return items.filter {
            when (it.destination) {
                BottomNavigationDestination.DISCOVER -> !hideDiscoverTab
                BottomNavigationDestination.SEARCH -> !forceToolbarSearch
                else -> true
            }
        }.toMutableList()
    }

    fun isBottomBarVisible(org: Boolean): Boolean {
        if (!org) {
            return org
        }

        return Flag.BOTTOM_NAVBAR_POSITION.asVariant<BottomNavbarPosition>() != BottomNavbarPosition.Hidden
    }

    fun attachToMainActivityWrapper(wrapper: ViewGroup?) {
        wrapper ?: return

        val frame = wrapper.getView<FrameLayout>(resName = "main_activity__main_wrapper")
        val bottomNavigation = wrapper.getView<FrameLayout>(resName = "bottom_navigation")

        when (Flag.BOTTOM_NAVBAR_POSITION.asVariant<BottomNavbarPosition>()) {
            BottomNavbarPosition.Top -> {
                wrapper.removeAllViews()
                wrapper.addView(bottomNavigation)
                wrapper.addView(frame)
            }
            BottomNavbarPosition.Hidden -> {
                bottomNavigation.changeVisibility(false)
            }
            else -> {}
        }
    }

    fun updateLogoutSectionRecyclerItem(viewHolder: RecyclerView.ViewHolder) {
        supportBridge.updateLogoutSectionRecyclerItem(viewHolder)
    }

    fun onChatViewPresenterConfigurationChanged(delegate: ChatViewDelegate) {
        if (!Flag.AUTO_HIDE_MESSAGE_INPUT.asBoolean()) {
            return
        }

        delegate.messageInputViewDelegate.apply {
            if (isLandscapeOrientation(context = context)) {
                hide()
            } else {
                show()
            }
        }
    }

    fun changeLandscapeChatContainerOpacity(viewGroup: ViewGroup?) {
        if (prefManager.isDarkThemeEnabled) {
            viewGroup?.setBackgroundColor(
                Color.argb(
                    (255 * (Flag.LANDSCAPE_CHAT_OPACITY.asInt() / 100F)).toInt(),
                    0,
                    0,
                    0
                )
            )
        }
    }

    fun getOrangeTvBuildTV(vh: View): TextView {
        return vh.getView("settings_logout_footer__orangetv_build")
    }

    fun getOrangeTvBuildDateTV(vh: View): TextView {
        return vh.getView("settings_logout_footer__orangetv_build_date")
    }

    fun isCreatorButtonVisible(state: Boolean): Boolean {
        if (Flag.HIDE_CREATE_BUTTON.asBoolean()) {
            return false
        }

        return state
    }

    fun tryBindUptime(uptime: TextView?, channelId: Int?): Disposable {
        uptime?.changeVisibility(false)
        channelId ?: run {
            LoggerImpl.error("channelId is null")
            return Completable.complete().subscribe()
        }

        uptime ?: run {
            LoggerImpl.error("uptime is null")
            return Completable.complete().subscribe()
        }

        return Completable.complete().subscribe()
    }

    override fun onCreateFeature() {}

    fun maybeHideCreateClipButton(createClipButton: ImageView) {
        if (Flag.HIDE_PLAYER_CREATE_CLIP_BUTTON.asBoolean()) {
            createClipButton.changeVisibility(false)
        }
    }

    fun maybeHideLiveShareButton(
        streamOverlayConfiguration: StreamOverlayConfiguration?,
        viewDelegate: PlayerOverlayViewDelegate?
    ) {
        streamOverlayConfiguration ?: return
        viewDelegate ?: return

        if (streamOverlayConfiguration == StreamOverlayConfiguration.SingleStream.INSTANCE) {
            if (Flag.HIDE_PLAYER_LIVE_SHARE_BUTTON.asBoolean()) {
                viewDelegate.shareButton.changeVisibility(false)
            }
        }
    }
}
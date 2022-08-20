package tv.orange.features.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import tv.orange.core.Core
import tv.orange.core.PreferenceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.BottomNavbarPosition
import tv.orange.core.util.ViewUtil.getView
import tv.orange.features.ui.di.scope.UIScope
import tv.orange.models.abc.Feature
import tv.twitch.android.core.strings.DateUtil
import tv.twitch.android.shared.chat.ChatViewDelegate
import tv.twitch.android.shared.ui.elements.navigation.BottomNavigationDestination
import tv.twitch.android.shared.ui.elements.navigation.BottomNavigationItem
import tv.twitch.android.shared.ui.menus.LogoutSectionRecyclerItem
import java.util.*
import javax.inject.Inject

@UIScope
class UI @Inject constructor(
    val context: Context
) : Feature {
    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}

    companion object {
        @JvmStatic
        fun get() = Core.getFeature(UI::class.java)

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

        private fun isLandscapeOrientation(context: Context): Boolean =
            context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        @JvmStatic
        fun shouldHideMessageInput(context: Context): Boolean {
            return Flag.AUTO_HIDE_MESSAGE_INPUT.asBoolean() && isLandscapeOrientation(context)
        }
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

        val frame = wrapper.getView<FrameLayout>("main_activity__main_wrapper")
        val bottomNavigation = wrapper.getView<FrameLayout>("bottom_navigation")

        when (Flag.BOTTOM_NAVBAR_POSITION.asVariant<BottomNavbarPosition>()) {
            BottomNavbarPosition.Top -> {
                wrapper.removeAllViews()
                wrapper.addView(bottomNavigation)
                wrapper.addView(frame)
            }
            BottomNavbarPosition.Hidden -> {
                bottomNavigation.visibility = View.GONE
            }
            else -> {}
        }
    }

    fun updateLogoutSectionRecyclerItem(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is LogoutSectionRecyclerItem.SectionFooterRecyclerItemViewHolder) {
            Core.get().buildConfig.let { config ->
                viewHolder.versionTextView.text = if (config.number > 0) {
                    "OrangeTV/${config.number}"
                } else {
                    "OrangeTV/Stub"
                }

                viewHolder.buildDateTextView.text = DateUtil.Companion!!.localizedDate(
                    context,
                    if (config.timestamp > 0) {
                        config.timestampToDate()
                    } else {
                        Date(0)
                    }
                )
            }
        }
    }

    fun onChatViewPresenterConfigurationChanged(delegate: ChatViewDelegate) {
        if (!Flag.AUTO_HIDE_MESSAGE_INPUT.asBoolean()) {
            return
        }

        delegate.messageInputViewDelegate.apply {
            if (isLandscapeOrientation(context)) {
                hide()
            } else {
                show()
            }
        }
    }

    fun changeLandscapeChatContainerOpacity(viewGroup: ViewGroup?) {
        if (PreferenceManager.isDarkTheme) {
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
}
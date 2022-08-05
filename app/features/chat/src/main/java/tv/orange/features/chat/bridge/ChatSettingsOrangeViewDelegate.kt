package tv.orange.features.chat.bridge

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.ViewUtil.getView
import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean
import tv.orange.features.chat.ChatHookProvider
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState
import tv.twitch.android.shared.chat.settings.entry.ChatSettingsViewDelegate
import tv.twitch.android.shared.chat.settings.viewutil.SettingsViewDelegateItemsUtilKt
import tv.twitch.android.shared.ui.menus.infomenu.InfoMenuViewDelegate
import tv.twitch.android.shared.ui.menus.togglemenu.SimpleToggleRowViewDelegate

class ChatSettingsOrangeViewDelegate(context: Context, view: View) {
    private val modStuffContainer: ViewGroup = view.getView("orange_preferences_container")

    private val refreshEmotes = SettingsViewDelegateItemsUtilKt.getValueRowItem(
        context,
        modStuffContainer,
    )
    private val toggleBttvEmotes = getToggleRowItem(
        context,
        modStuffContainer,
        ResourceManager.get().getStringId("orange_settings_bttv_emotes")
    )
    private val toggleFfzEmotes = getToggleRowItem(
        context,
        modStuffContainer,
        ResourceManager.get().getStringId("orange_settings_ffz_emotes")
    )
    private val toggleStvEmotes = getToggleRowItem(
        context,
        modStuffContainer,
        ResourceManager.get().getStringId("orange_settings_stv_emotes")
    )

    private fun getToggleRowItem(
        context: Context,
        container: ViewGroup,
        resId: Int
    ): SimpleToggleRowViewDelegate {
        val inflate = LayoutInflater.from(context).inflate(
            ResourceManager.get().getStringId("toggle_row_item"),
            container,
            false
        )
        return SimpleToggleRowViewDelegate(inflate, resId)
    }

    init {
        modStuffContainer.addView(refreshEmotes.contentView)
        modStuffContainer.addView(toggleBttvEmotes.contentView)
        modStuffContainer.addView(toggleFfzEmotes.contentView)
        modStuffContainer.addView(toggleStvEmotes.contentView)
        renderEmotesState(
            Flag.BTTV_EMOTES.valueBoolean(),
            Flag.FFZ_EMOTES.valueBoolean(),
            Flag.STV_EMOTES.valueBoolean()
        )
    }

    fun render(state: ViewDelegateState) {
        refreshEmotes.render(
            InfoMenuViewDelegate.State(
                ResourceManager.get().getString("orange_chat_settings_refresh"),
                null,
                null
            )
        )
    }

    private fun renderEmotesState(bttv: Boolean, ffz: Boolean, stv: Boolean) {
        toggleBttvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(bttv))
        toggleFfzEmotes.render(SimpleToggleRowViewDelegate.ToggleState(ffz))
        toggleStvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(stv))
    }

    private fun rebuildEmotes() {
        ChatHookProvider.get().rebuildEmotes()
    }

    fun injectEvents(listOf: List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>>): List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>> {
        return listOf.toMutableList().apply {
            add(refreshEmotes.eventObserver().doOnNext {
                rebuildEmotes()
            }.map {
                ChatSettingsOrangeEvents.Closable()
            })
            add(toggleBttvEmotes.eventObserver().doOnNext {
                PreferenceManager.get().writeBoolean(Flag.BTTV_EMOTES, it.isToggled)
                renderEmotesState(
                    it.isToggled,
                    Flag.FFZ_EMOTES.valueBoolean(),
                    Flag.STV_EMOTES.valueBoolean()
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleFfzEmotes.eventObserver().doOnNext {
                PreferenceManager.get().writeBoolean(Flag.FFZ_EMOTES, it.isToggled)
                renderEmotesState(
                    Flag.BTTV_EMOTES.valueBoolean(),
                    it.isToggled,
                    Flag.STV_EMOTES.valueBoolean()
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleStvEmotes.eventObserver().doOnNext {
                PreferenceManager.get().writeBoolean(Flag.STV_EMOTES, it.isToggled)
                renderEmotesState(
                    Flag.BTTV_EMOTES.valueBoolean(),
                    Flag.FFZ_EMOTES.valueBoolean(),
                    it.isToggled
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
        }
    }
}
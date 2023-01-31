package tv.orange.features.chat.bridge

import android.content.Context
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.models.flag.Flag.Companion.setValue
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.chat.ChatHookProvider
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState
import tv.twitch.android.shared.chat.settings.entry.ChatSettingsViewDelegate
import tv.twitch.android.shared.chat.settings.viewutil.SettingsViewDelegateItemsUtilKt
import tv.twitch.android.shared.ui.menus.infomenu.InfoMenuViewDelegate
import tv.twitch.android.shared.ui.menus.togglemenu.SimpleToggleRowViewDelegate

class ChatSettingsOrangeViewDelegate(context: Context, view: View) {
    private val resourceManager = ResourcesManagerCore.get()

    private val modStuffContainer: ViewGroup = view.getView(
        resName = "orange_preferences_container"
    )
    private val refreshEmotes = SettingsViewDelegateItemsUtilKt.getValueRowItem(
        context,
        modStuffContainer,
    )
    private val toggleBttvEmotes = getToggleRowItem(
        modStuffContainer,
        resourceManager.getStringId(resName = "orange_settings_bttv_emotes")
    )
    private val toggleFfzEmotes = getToggleRowItem(
        modStuffContainer,
        resourceManager.getStringId(resName = "orange_settings_ffz_emotes")
    )
    private val toggleStvEmotes = getToggleRowItem(
        modStuffContainer,
        resourceManager.getStringId(resName = "orange_settings_stv_emotes")
    )
    private val toggleItzEmotes = getToggleRowItem(
        modStuffContainer,
        resourceManager.getStringId(resName = "orange_settings_itz_emotes")
    )
    private val killChat = getToggleRowItem(
        modStuffContainer,
        resourceManager.getStringId(resName = "orange_chat_kill_chat")
    )

    private fun getToggleRowItem(
        container: ViewGroup,
        resId: Int
    ): SimpleToggleRowViewDelegate {
        return SimpleToggleRowViewDelegate(container.inflate(resName = "toggle_row_item"), resId)
    }

    init {
        modStuffContainer.addView(refreshEmotes.contentView)
        modStuffContainer.addView(toggleBttvEmotes.contentView)
        modStuffContainer.addView(toggleFfzEmotes.contentView)
        modStuffContainer.addView(toggleStvEmotes.contentView)
        modStuffContainer.addView(toggleItzEmotes.contentView)
        modStuffContainer.addView(killChat.contentView)
        renderEmotesState()
    }

    fun render(state: ViewDelegateState) {
        refreshEmotes.render(
            InfoMenuViewDelegate.State(
                resourceManager.getString(resName = "orange_chat_settings_refresh"),
                null,
                null
            )
        )
        killChat.render(SimpleToggleRowViewDelegate.ToggleState(ChatHookProvider.isChatKilled))
    }

    private fun renderEmotesState() {
        toggleBttvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(Flag.BTTV_EMOTES.asBoolean()))
        toggleFfzEmotes.render(SimpleToggleRowViewDelegate.ToggleState(Flag.FFZ_EMOTES.asBoolean()))
        toggleStvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(Flag.STV_EMOTES.asBoolean()))
        toggleItzEmotes.render(SimpleToggleRowViewDelegate.ToggleState(Flag.ITZ_EMOTES.asBoolean()))
    }

    fun injectEvents(listOf: List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>>): List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>> {
        return listOf.toMutableList().apply {
            add(refreshEmotes.eventObserver().doOnNext {
                ChatHookProvider.get().rebuildEmotes()
            }.map {
                ChatSettingsOrangeEvents.Closable()
            })
            add(killChat.eventObserver().doOnNext {
                ChatHookProvider.get().switchKillChat()
            }.map {
                killChat.render(SimpleToggleRowViewDelegate.ToggleState(ChatHookProvider.isChatKilled))
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleBttvEmotes.eventObserver().doOnNext {
                Flag.BTTV_EMOTES.setValue(it.isToggled)
                renderEmotesState()
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleFfzEmotes.eventObserver().doOnNext {
                Flag.FFZ_EMOTES.setValue(it.isToggled)
                renderEmotesState()
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleStvEmotes.eventObserver().doOnNext {
                Flag.STV_EMOTES.setValue(it.isToggled)
                renderEmotesState()
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleItzEmotes.eventObserver().doOnNext {
                Flag.ITZ_EMOTES.setValue(it.isToggled)
                renderEmotesState()
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
        }
    }
}
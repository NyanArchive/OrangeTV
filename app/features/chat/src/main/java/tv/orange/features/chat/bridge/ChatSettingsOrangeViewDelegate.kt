package tv.orange.features.chat.bridge

import android.content.Context
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import tv.orange.core.PreferenceManager
import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.orange.core.util.ViewUtil.getView
import tv.orange.core.util.ViewUtil.inflate
import tv.orange.features.chat.ChatHookProvider
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState
import tv.twitch.android.shared.chat.settings.entry.ChatSettingsViewDelegate
import tv.twitch.android.shared.chat.settings.viewutil.SettingsViewDelegateItemsUtilKt
import tv.twitch.android.shared.ui.menus.infomenu.InfoMenuViewDelegate
import tv.twitch.android.shared.ui.menus.togglemenu.SimpleToggleRowViewDelegate

class ChatSettingsOrangeViewDelegate(context: Context, view: View) {
    private val resourceManager = ResourceManager.get()
    private val preferenceManager = PreferenceManager.get()

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
        renderEmotesState(
            bttvState = Flag.BTTV_EMOTES.asBoolean(),
            ffzState = Flag.FFZ_EMOTES.asBoolean(),
            stvState = Flag.STV_EMOTES.asBoolean()
        )
    }

    fun render(state: ViewDelegateState) {
        refreshEmotes.render(
            InfoMenuViewDelegate.State(
                resourceManager.getString(resName = "orange_chat_settings_refresh"),
                null,
                null
            )
        )
    }

    private fun renderEmotesState(bttvState: Boolean, ffzState: Boolean, stvState: Boolean) {
        toggleBttvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(bttvState))
        toggleFfzEmotes.render(SimpleToggleRowViewDelegate.ToggleState(ffzState))
        toggleStvEmotes.render(SimpleToggleRowViewDelegate.ToggleState(stvState))
    }

    fun injectEvents(listOf: List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>>): List<Flowable<ChatSettingsViewDelegate.ChatSettingsEvents>> {
        return listOf.toMutableList().apply {
            add(refreshEmotes.eventObserver().doOnNext {
                ChatHookProvider.get().rebuildEmotes()
            }.map {
                ChatSettingsOrangeEvents.Closable()
            })
            add(toggleBttvEmotes.eventObserver().doOnNext {
                preferenceManager.writeBoolean(
                    flag = Flag.BTTV_EMOTES,
                    value = it.isToggled
                )
                renderEmotesState(
                    bttvState = it.isToggled,
                    ffzState = Flag.FFZ_EMOTES.asBoolean(),
                    stvState = Flag.STV_EMOTES.asBoolean()
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleFfzEmotes.eventObserver().doOnNext {
                preferenceManager.writeBoolean(
                    flag = Flag.FFZ_EMOTES,
                    value = it.isToggled
                )
                renderEmotesState(
                    bttvState = Flag.BTTV_EMOTES.asBoolean(),
                    ffzState = it.isToggled,
                    stvState = Flag.STV_EMOTES.asBoolean()
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
            add(toggleStvEmotes.eventObserver().doOnNext {
                preferenceManager.writeBoolean(
                    flag = Flag.STV_EMOTES,
                    value = it.isToggled
                )
                renderEmotesState(
                    bttvState = Flag.BTTV_EMOTES.asBoolean(),
                    ffzState = Flag.FFZ_EMOTES.asBoolean(),
                    stvState = it.isToggled
                )
            }.map {
                ChatSettingsOrangeEvents.Toggle()
            })
        }
    }
}
package tv.orange.features.highlighter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import tv.orange.core.PreferenceManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asInt
import tv.orange.features.highlighter.data.model.HighlightDesc
import tv.orange.features.highlighter.view.HighlighterFragment
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

class Highlighter @Inject constructor(
    val highlighterDelegate: HighlighterDelegate,
    val prefManager: PreferenceManagerCore
) {
    fun getHighlightDesc(cmi: ChatMessageInterface): HighlightDesc? {
        return highlighterDelegate.getHighlightDesc(cmi)
    }

    fun createHighlighterFragment(): Fragment {
        return HighlighterFragment.newInstance()
    }

    fun isEnabled(): Boolean {
        return highlighterDelegate.isEnabled
    }

    fun dispose() {
        highlighterDelegate.dispose()
    }

    fun pull() {
        highlighterDelegate.pull()
    }

    fun showChangeMentionHighlightColorDialog(activity: FragmentActivity) {
        val dialog = ColorPickerDialog.newBuilder()
            .setDialogId(1)
            .setColor(Flag.USER_MENTION_COLOR.asInt())
            .create()

        dialog.addColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, newColor: Int) {
                prefManager.setUserMentionColor(newColor)
            }

            override fun onDialogDismissed(dialogId: Int) {}
        })
        dialog.show(activity.supportFragmentManager, "orange_change_color")
    }
}
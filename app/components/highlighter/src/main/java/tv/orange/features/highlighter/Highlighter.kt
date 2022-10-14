package tv.orange.features.highlighter

import androidx.fragment.app.Fragment
import tv.orange.features.highlighter.data.model.HighlightDesc
import tv.orange.features.highlighter.view.HighlighterFragment
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

class Highlighter @Inject constructor(
    val highlighterDelegate: HighlighterDelegate
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
}
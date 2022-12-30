package tv.orange.features.blacklist

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.Fragment
import tv.orange.features.blacklist.view.BlacklistFragment
import tv.twitch.chat.ChatLiveMessage
import javax.inject.Inject

class Blacklist @Inject constructor(
    val highlighterDelegate: BlacklistDelegate
) {
    fun isBlacklisted(cmi: ChatLiveMessage): Boolean {
        return highlighterDelegate.isBlacklisted(cmi)
    }

    fun createBlacklistFragment(): Fragment {
        return BlacklistFragment.newInstance()
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
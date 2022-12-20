package tv.orange.features.blacklist

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.features.blacklist.data.model.KeywordData
import tv.orange.features.blacklist.data.source.BlacklistSource
import tv.twitch.chat.ChatEmoticonToken
import tv.twitch.chat.ChatLiveMessage
import tv.twitch.chat.ChatTextToken
import tv.twitch.chat.ChatUrlToken
import javax.inject.Inject

class BlacklistDelegate @Inject constructor(val source: BlacklistSource) {
    private val disposables = CompositeDisposable()

    var usernames = HashSet<String>()
    var sensitive = HashSet<String>()
    var insensitive = HashSet<String>()

    var isEnabled: Boolean = false

    fun pull() {
        disposables.add(
            source.getFlow().subscribeOn(Schedulers.single())
                .subscribe({ keywords ->
                    val usernames = HashSet<String>()
                    val sensitive = HashSet<String>()
                    val insensitive = HashSet<String>()
                    keywords?.forEach {
                        when (it.type) {
                            KeywordData.Type.INSENSITIVE -> {
                                insensitive.add(it.word.lowercase())
                            }
                            KeywordData.Type.CASESENSITIVE -> {
                                sensitive.add(it.word)
                            }
                            KeywordData.Type.USERNAME -> {
                                usernames.add(it.word.lowercase())
                            }
                        }
                    }

                    this@BlacklistDelegate.usernames = usernames
                    this@BlacklistDelegate.insensitive = insensitive
                    this@BlacklistDelegate.sensitive = sensitive

                    isEnabled =
                        usernames.isNotEmpty() || sensitive.isNotEmpty() || insensitive.isNotEmpty()
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun isBlacklisted(clm: ChatLiveMessage): Boolean {
        if (!isEnabled) {
            return false
        }

        val cmi = clm.messageInfo

        if (usernames.contains(cmi.userName.lowercase())) {
            return true
        }

        cmi.tokens.forEach { token ->
            when (token) {
                is ChatTextToken -> {
                    token.text.split("\\s+".toRegex()).map { it.trim(' ') }
                        .forEach { word ->
                            if (word.isNotBlank()) {
                                if (sensitive.contains(word)) {
                                    return true
                                }
                                if (insensitive.contains(word)) {
                                    return true
                                }
                            }
                        }
                }
                is ChatEmoticonToken -> {
                    val word = token.emoticonText
                    if (word.isNotBlank()) {
                        if (sensitive.contains(word)) {
                            return true
                        }
                        if (insensitive.contains(word)) {
                            return true
                        }
                    }
                }
                is ChatUrlToken -> {
                    val word = token.url
                    if (word.isNotBlank()) {
                        if (sensitive.contains(word)) {
                            return true
                        }
                        if (insensitive.contains(word)) {
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    fun dispose() {
        disposables.clear()
    }
}
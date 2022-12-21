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

                    isEnabled = usernames.isNotEmpty() ||
                            sensitive.isNotEmpty() ||
                            insensitive.isNotEmpty()
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
                    if (token.text.split(SPLIT_PATTERN)
                            .map { it.trim(' ') }
                            .any { isBlacklisted(it) }
                    ) {
                        return true
                    }
                }
                is ChatEmoticonToken -> {
                    if (isBlacklisted(token.emoticonText)) {
                        return true
                    }
                }
                is ChatUrlToken -> {
                    if (isBlacklisted(token.url)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun isBlacklisted(word: String?): Boolean {
        word ?: return false

        if (word.isNotBlank()) {
            if (sensitive.contains(word)) {
                return true
            }
            if (insensitive.contains(word.lowercase())) {
                return true
            }
        }

        return false
    }

    fun dispose() {
        disposables.clear()
    }

    companion object {
        private val SPLIT_PATTERN = "[\\s,.!?-]".toRegex()
    }
}
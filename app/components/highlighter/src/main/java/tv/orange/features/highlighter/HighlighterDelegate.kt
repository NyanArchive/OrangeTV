package tv.orange.features.highlighter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.Logger
import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.data.source.HighlighterSource
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.provider.chat.ChatMessageInterface
import javax.inject.Inject

class HighlighterDelegate @Inject constructor(val source: HighlighterSource) {
    private val disposables = CompositeDisposable()

    var usernames = LinkedHashMap<String, Int>()
    var sensitive = LinkedHashMap<String, Int>()
    var insensitive = LinkedHashMap<String, Int>()

    var isEnabled: Boolean = false

    fun pull() {
        disposables.add(
            source.getFlow().subscribeOn(Schedulers.single())
                .subscribe({ keywords ->
                    val usernames = LinkedHashMap<String, Int>()
                    val sensitive = LinkedHashMap<String, Int>()
                    val insensitive = LinkedHashMap<String, Int>()
                    keywords?.forEach {
                        when (it.type) {
                            KeywordData.Type.INSENSITIVE -> {
                                insensitive[it.word.lowercase()] = it.color
                            }
                            KeywordData.Type.CASESENSITIVE -> {
                                sensitive[it.word] = it.color
                            }
                            KeywordData.Type.USERNAME -> {
                                usernames[it.word.lowercase()] = it.color
                            }
                        }
                    }

                    this@HighlighterDelegate.usernames = usernames
                    this@HighlighterDelegate.insensitive = insensitive
                    this@HighlighterDelegate.sensitive = sensitive

                    isEnabled =
                        usernames.isNotEmpty() || sensitive.isNotEmpty() || insensitive.isNotEmpty()
                    Logger.debug("Update!")
                }, {
                    it.printStackTrace()
                })
        )
    }


    fun getHighlightColor(cmi: ChatMessageInterface): Int? {
        if (!isEnabled) {
            return null
        }

        usernames[cmi.userName.lowercase()]?.let { color ->
            return color
        }

        cmi.tokens.forEach { token ->
            if (token is MessageToken.TextToken) {
                token.text.split("\\s+".toRegex()).map { it.trim(' ', '.', ',', '?', '!') }
                    .forEach { word ->
                        if (word.isNotBlank()) {
                            sensitive[word]?.let { color ->
                                return color
                            }
                            insensitive[word]?.let { color ->
                                return color
                            }
                        }
                    }
            }
        }

        return null
    }

    fun dispose() {
        disposables.clear()
    }
}
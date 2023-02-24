package tv.orange.features.highlighter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.features.highlighter.data.model.HighlightDesc
import tv.orange.features.highlighter.data.model.KeywordData
import tv.orange.features.highlighter.data.source.HighlighterSource
import tv.twitch.android.models.chat.MessageToken
import tv.twitch.android.shared.chat.pub.ChatMessageInterface
import javax.inject.Inject

class HighlighterDelegate @Inject constructor(val source: HighlighterSource) {
    private val disposables = CompositeDisposable()

    var usernames = LinkedHashMap<String, HighlightDesc>()
    var sensitive = LinkedHashMap<String, HighlightDesc>()
    var insensitive = LinkedHashMap<String, HighlightDesc>()

    var isEnabled: Boolean = false

    fun pull() {
        disposables.add(
            source.getFlow().subscribeOn(Schedulers.single())
                .subscribe({ keywords ->
                    val usernames = LinkedHashMap<String, HighlightDesc>()
                    val sensitive = LinkedHashMap<String, HighlightDesc>()
                    val insensitive = LinkedHashMap<String, HighlightDesc>()
                    keywords?.forEach {
                        when (it.type) {
                            KeywordData.Type.INSENSITIVE -> {
                                insensitive[it.word.lowercase()] =
                                    HighlightDesc(it.color, it.vibration)
                            }
                            KeywordData.Type.CASESENSITIVE -> {
                                sensitive[it.word] = HighlightDesc(it.color, it.vibration)
                            }
                            KeywordData.Type.USERNAME -> {
                                usernames[it.word.lowercase()] =
                                    HighlightDesc(it.color, it.vibration)
                            }
                        }
                    }

                    this@HighlighterDelegate.usernames = usernames
                    this@HighlighterDelegate.insensitive = insensitive
                    this@HighlighterDelegate.sensitive = sensitive

                    isEnabled =
                        usernames.isNotEmpty() || sensitive.isNotEmpty() || insensitive.isNotEmpty()
                }, {
                    it.printStackTrace()
                })
        )
    }


    fun getHighlightDesc(cmi: ChatMessageInterface): HighlightDesc? {
        if (!isEnabled) {
            return null
        }

        usernames[cmi.userName.lowercase()]?.let { color ->
            return color
        }

        cmi.tokens.forEach { token ->
            if (token is MessageToken.TextToken) {
                token.text.split("\\s+".toRegex()).map { it.trim(' ') }
                    .forEach { word ->
                        if (word.isNotBlank()) {
                            sensitive[word]?.let { color ->
                                return color
                            }
                            insensitive[word.lowercase()]?.let { color ->
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
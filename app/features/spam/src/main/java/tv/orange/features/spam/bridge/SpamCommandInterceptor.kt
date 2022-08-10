package tv.orange.features.spam.bridge

import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandAction
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import tv.twitch.android.shared.chat.model.ChatSendAction
import java.util.concurrent.TimeUnit

class SpamCommandInterceptor(
    private val chatSource: LiveChatSource
) : ChatCommandInterceptor {
    private val disposable = CompositeDisposable()

    private fun simpleMessageSpammer(count: Int, delay: Long, text: String): Flowable<Long> {
        return Flowable.intervalRange(0, count.toLong(), 0, delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext { i ->
                val textToSpam = if (i % 2 == 0L) {
                    text
                } else {
                    changeTextToSpam(text)
                }
                chatSource.sendMessage(textToSpam, ChatSendAction.CLICK)
            }
    }

    override fun executeChatCommand(action: ChatCommandAction?) {
        when (action) {
            is ChatSpamCommand -> {
                disposable.add(
                    simpleMessageSpammer(
                        count = action.count,
                        delay = action.delay,
                        text = action.messageText
                    ).subscribe()
                )
            }
            is ChatSpamErrorCommand -> {
                chatSource.addSystemMessage("spam: ${action.text}", false, null)
            }
        }
    }

    override fun onDestroy() {
        disposable.clear()
    }

    override fun parseChatCommand(
        strArr: Array<out String>?,
        channelId: Int?,
        p2: Long?
    ): ChatCommandAction {
        strArr ?: return ChatCommandAction.NoOp.INSTANCE

        if (strArr.size == 1 && strArr[0] == "/spam") {
            return ChatSpamErrorCommand("Usage: /spam {count} {delay} {text} [*{num}]")
        }

        if (strArr.size < 4) {
            return ChatCommandAction.NoOp.INSTANCE
        }

        var command = strArr[0]
        if (command.isBlank()) {
            return ChatCommandAction.NoOp.INSTANCE
        }

        if (!command.equals("/spam", ignoreCase = true)) {
            return ChatCommandAction.NoOp.INSTANCE
        }

        command = strArr[1]
        val count = parseSpamCount(command)
            ?: return ChatSpamErrorCommand("Wrong {count} param: '$command'")
        command = strArr[2]
        val delay = parseSpamDelay(command)
            ?: return ChatSpamErrorCommand("Wrong {delay} param: '$command'")

        var text = getMultiplier(strArr)?.let { num ->
            val tmp = TextUtils.join(" ", strArr.copyOfRange(3, strArr.size - 1))
            if (tmp.endsWith(" ")) {
                tmp.repeat(num)
            } else {
                "$tmp ".repeat(num).removeSuffix(" ")
            }
        } ?: TextUtils.join(" ", strArr.copyOfRange(3, strArr.size))

        if (text.isBlank()) {
            return ChatSpamErrorCommand("Nothing to spam")
        }
        if (text.length > 498) {
            text = text.substring(0, 498)
        }

        return ChatSpamCommand(
            count = count,
            delay = delay,
            messageText = text
        )
    }

    companion object {
        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        private fun changeTextToSpam(org: String): String {
            val f = org.trim()
            if (!f.contentEquals(org)) {
                return f
            }

            return if (f.contains(' ')) {
                (org as java.lang.String).replaceFirst(" ", "  ")
            } else {
                "$org ."
            }
        }

        private fun getMultiplier(arr: Array<out String>): Int? {
            if (arr.size < 5) {
                return null
            }
            val text = arr[arr.size - 1]
            if (text.length < 2) {
                return null
            }

            if (text[0] != '*') {
                return null
            }

            return text.substring(1).toIntOrNull()
        }

        private fun parseSpamCount(text: String): Int? {
            val value = text.toIntOrNull() ?: return null

            if (value <= 0) {
                return 1
            }

            if (value > 100) {
                return 100
            }

            return value
        }

        private fun parseSpamDelay(text: String): Long? {
            val value = text.toIntOrNull() ?: return null

            if (value <= 0) {
                return 250L
            }

            if (value > 100) {
                return 100 * 1000L
            }

            return value * 1000L
        }
    }
}
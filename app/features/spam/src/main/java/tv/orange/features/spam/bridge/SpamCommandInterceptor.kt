package tv.orange.features.spam.bridge

import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tv.orange.core.ResourcesManagerCore
import tv.twitch.android.models.channel.ChannelInfo
import tv.twitch.android.shared.chat.LiveChatSource
import tv.twitch.android.shared.chat.command.ChatCommandAction
import tv.twitch.android.shared.chat.command.ChatCommandInterceptor
import tv.twitch.android.shared.chat.model.ChatSendAction
import java.util.concurrent.TimeUnit

class SpamCommandInterceptor(
    private val chatSource: LiveChatSource,
    private val rm: ResourcesManagerCore
) : ChatCommandInterceptor {
    private val disposable = CompositeDisposable()

    private fun pyramidSpammer(width: Int, emote: String, delay: Long): Flowable<Long> {
        val spamCount = width * 2 - 1
        val maxW = kotlin.math.ceil(spamCount.toDouble().div(2)).toInt()
        return Flowable.intervalRange(1, spamCount.toLong() + 1, 0, delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext { i ->
                val repeat = if (i <= maxW) {
                    i.toInt()
                } else {
                    spamCount - i.toInt() + 1
                }
                chatSource.sendMessage("$emote ".repeat(repeat), ChatSendAction.CLICK)
            }
    }

    private fun simpleMessageSpammer(count: Int, delay: Long, text: String): Flowable<Long> {
        return Flowable.intervalRange(0, count.toLong(), 0, delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext { i ->
                val textToSpam = if (i % 2 == 0L) {
                    text
                } else {
                    changeTextToSpam(text = text)
                }
                chatSource.sendMessage(textToSpam, ChatSendAction.CLICK)
            }
    }

    override fun executeChatCommand(action: ChatCommandAction?) {
        when (action) {
            is ChatSpamPyramidCommand -> {
                disposable.add(
                    pyramidSpammer(
                        width = action.width,
                        emote = action.emote,
                        delay = action.delay
                    ).subscribe()
                )
            }
            is ChatSpamCommandImpl -> {
                disposable.add(
                    simpleMessageSpammer(
                        count = action.count,
                        delay = action.delay,
                        text = action.messageText.trim()
                    ).subscribe()
                )
            }
            is ChatSpamErrorCommand -> {
                chatSource.addSystemMessage(
                    rm.getString("orange_generic_error", action.text),
                    false,
                    null
                )
            }
        }
    }

    override fun onDestroy() {
        disposable.clear()
    }

    override fun parseChatCommand(
        strArr: Array<out String>?,
        p1: ChannelInfo?,
        p2: Long?
    ): ChatCommandAction {
        strArr ?: return ChatCommandAction.NoOp.INSTANCE

        if (isPyramidSpam(strArr)) {
            return parsePyramidChatCommand(strArr, p1, p2)
        }

        if (showSpamTutor(strArr)) {
            return ChatSpamErrorCommand(text = rm.getString("orange_generic_spam_usage", "/spam {count} {delay} {text} [*{num}]"))
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
        val count = parseSpamCount(text = command)
            ?: return ChatSpamErrorCommand(text = rm.getString("orange_spam_error_wc", command))
        command = strArr[2]
        val delay = parseSpamDelay(text = command)
            ?: return ChatSpamErrorCommand(text = rm.getString("orange_spam_error_wd", command))

        var text = getMultiplier(strArr)?.let { num ->
            val tmp = TextUtils.join(" ", strArr.copyOfRange(3, strArr.size - 1))
            if (tmp.endsWith(" ")) {
                tmp.repeat(num)
            } else {
                "$tmp ".repeat(num).removeSuffix(" ")
            }
        } ?: TextUtils.join(" ", strArr.copyOfRange(3, strArr.size))

        if (text.isBlank()) {
            return ChatSpamErrorCommand(text = rm.getString("orange_spam_error_empty"))
        }

        if (text.length > 498) {
            text = text.substring(0, 498)
        }

        return ChatSpamCommandImpl(
            count = count,
            delay = delay,
            messageText = text
        )
    }

    private fun parsePyramidChatCommand(
        strArr: Array<out String>,
        p1: ChannelInfo?,
        p2: Long?
    ): ChatCommandAction {
        if (strArr.size != 5) {
            return ChatSpamErrorCommand(text = rm.getString("orange_generic_spam_usage", "/spam pyramid {width} {emote} {delay}"))
        }

        var command = strArr[2]
        val count = parseSpamCount(text = command)
            ?: return ChatSpamErrorCommand(text = rm.getString("orange_spam_error_ww", command))
        val emote = strArr[3]
        command = strArr[4]
        val delay = parseSpamDelay(text = command)
            ?: return ChatSpamErrorCommand(text = rm.getString("orange_spam_error_wd", command))

        return ChatSpamPyramidCommand(
            emote = emote,
            delay = delay,
            width = count
        )
    }

    private fun isPyramidSpam(strArr: Array<out String>): Boolean {
        if (strArr.size < 2) {
            return false
        }

        if (strArr[0].trim().lowercase() != "/spam") {
            return false
        }

        if (strArr[1].trim().lowercase() != "pyramid") {
            return false
        }

        return true
    }

    companion object {
        private fun showSpamTutor(strArr: Array<out String>): Boolean {
            if (strArr.isEmpty()) {
                return false
            }

            if (strArr[0].trim().lowercase() != "/spam") {
                return false
            }

            if (strArr.size < 4) {
                return true
            }

            return false
        }

        @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
        private fun changeTextToSpam(text: String): String {
            val f = text.trim()
            if (!f.contentEquals(text)) {
                return f
            }

            return if (f.contains(' ')) {
                (text as java.lang.String).replaceFirst(" ", "  ")
            } else {
                "$text ."
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
            val value = text.toDoubleOrNull() ?: return null

            if (value <= 0) {
                return 150L
            }

            if (value > 100) {
                return 100 * 1000L
            }

            return (value * 1000).toLong()
        }
    }
}
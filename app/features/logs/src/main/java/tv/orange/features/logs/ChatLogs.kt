package tv.orange.features.logs

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import tv.orange.core.Core
import tv.orange.core.Logger
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asVariant
import tv.orange.core.models.flag.variants.LocalLogs
import tv.orange.core.util.ViewUtil.changeVisibility
import tv.orange.features.logs.component.data.repository.LogsRepository
import tv.orange.features.logs.view.ViewFactory
import tv.orange.models.abc.Feature
import tv.twitch.android.core.mvp.presenter.BasePresenter
import tv.twitch.android.core.mvp.rxutil.DisposeOn
import tv.twitch.android.models.chat.MessageBadge
import tv.twitch.android.models.social.ChatUser
import tv.twitch.android.provider.chat.events.MessagesReceivedEvent
import tv.twitch.android.provider.chat.model.ChatHistoryMessage
import tv.twitch.android.shared.chat.ChatMessageParser
import tv.twitch.android.shared.chat.chatuserdialog.ChatUserDialogInfo
import tv.twitch.android.shared.chat.moderation.ModerationActionBottomSheetViewDelegate
import tv.twitch.android.shared.chat.moderation.ModerationBottomSheetViewState
import tv.twitch.android.shared.ui.elements.bottomsheet.BottomSheetListItemModel
import tv.twitch.chat.ChatLiveMessage
import javax.inject.Inject

class ChatLogs @Inject constructor(
    val viewFactory: ViewFactory,
    val logsRepository: LogsRepository
) : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(ChatLogs::class.java)

        @JvmStatic
        fun destroy() {
            Core.destroyFeature(ChatLogs::class.java)
        }
    }

    fun setupLocalLogsClickHandler(
        activity: FragmentActivity,
        localLogs: TextView,
        info: ChatUserDialogInfo,
        onDismissListener: () -> Unit,
        chatUser: ChatUser
    ) {
        localLogs.setOnClickListener {
            val fragment = viewFactory.createLogsFragment()
            fragment.bind(activity)
            fragment.show(activity.supportFragmentManager, "orange_logs")
            fragment.loadLocalLogs(chatUser.userId, info.channelId)
            onDismissListener.invoke()
        }
    }

    fun showModLogs(
        activity: FragmentActivity,
        event: ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent
    ): Boolean {
        if (event is ModerationActionBottomSheetViewDelegate.ModerationActionButtonEvent.ViewLogs) {
            val fragment = viewFactory.createLogsFragment()
            fragment.bind(activity)
            fragment.show(activity.supportFragmentManager, "orange_logs")
            fragment.loadTwitchLogs(event.userId, event.channelId)
            return true
        }

        return false
    }

    fun injectLocalLogsButton(view: View): TextView {
        return viewFactory.createLocalLogsButton(view).apply {
            this.changeVisibility(Flag.LOCAL_LOGS.asVariant<LocalLogs>() != LocalLogs.L0)
        }
    }

    fun injectModLogsButton(
        list: List<BottomSheetListItemModel<*>>,
        state: ModerationBottomSheetViewState
    ): List<BottomSheetListItemModel<*>> {
        return list.toMutableList().apply {
            add(viewFactory.createModLogsButton(state = state))
        }
    }

    override fun onDestroyFeature() {}
    override fun onCreateFeature() {}

    private fun maybeAddMessagesToLocalStore(channelId: Int, chatLiveMessageArr: List<ChatLiveMessage>) {
        if (Flag.LOCAL_LOGS.asVariant<LocalLogs>() == LocalLogs.L0) {
            return
        }

        val parser = ChatMessageParser()
        chatLiveMessageArr.forEach { msg ->
            logsRepository.addLocalMessage(
                channelId,
                msg.messageInfo.userId,
                msg.messageInfo.userName,
                msg.messageInfo.timestamp,
                ChatHistoryMessage(
                    msg.messageInfo.userId,
                    msg.messageInfo.userName,
                    msg.messageInfo.displayName,
                    msg.messageInfo.nameColorARGB.toString(),
                    msg.messageInfo.badges.map { chatMessageBadge ->
                        MessageBadge(chatMessageBadge.name, chatMessageBadge.version)
                    },
                    parser.tokens(msg.messageInfo.tokens)
                )
            )
        }
    }

    fun subscribeToMessages(
        presenter: BasePresenter,
        messagesSubject: PublishSubject<MessagesReceivedEvent>
    ) {
        presenter.asyncSubscribe(
            messagesSubject.toFlowable(BackpressureStrategy.BUFFER), {
                maybeAddMessagesToLocalStore(it.channelId, it.messages)
            }, {
                it.printStackTrace()
            }, DisposeOn.DESTROY
        )
    }
}
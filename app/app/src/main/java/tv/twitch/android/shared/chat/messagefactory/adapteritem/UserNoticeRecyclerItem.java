package tv.twitch.android.shared.chat.messagefactory.adapteritem;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.core.ViewExtensionsKt;
import tv.twitch.android.core.adapters.AbstractTwitchRecyclerViewHolder;
import tv.twitch.android.shared.ui.elements.GlideHelper;
import tv.twitch.android.shared.ui.elements.span.GlideChatImageCustomTarget;
import tv.twitch.android.util.StringUtils;

public class UserNoticeRecyclerItem {
    private ContextWrapper context;
    private Spanned messageSpan;
    private Spanned systemMessageSpan;

    /* ... */

    private final void setupMessageView(TextView textView, Spanned spanned, UserNoticeViewHolder holder) { // TODO: __REPLACE_METHOD
        ViewExtensionsKt.visibilityForBoolean(textView, !StringUtils.isEmpty(spanned));
        if (spanned != null) {
            List<GlideChatImageCustomTarget> targets = GlideHelper.loadImagesFromSpannedAndGetTargets(context, spanned, textView);
            textView.setText(spanned);
            holder.addTargetsToClear(targets);
        }
    }

    public void bindToViewHolder(RecyclerView.ViewHolder viewHolder) {
        /* ... */
        UserNoticeViewHolder userNoticeViewHolder = viewHolder instanceof UserNoticeViewHolder ? (UserNoticeViewHolder) viewHolder : null;
        if (userNoticeViewHolder != null) {
            /* ... */

            setupMessageView(userNoticeViewHolder.getSystemMessage(), this.systemMessageSpan, userNoticeViewHolder); // TODO: __REPLACE_CODE
            setupMessageView(userNoticeViewHolder.getChatMessage(), this.messageSpan, userNoticeViewHolder); // TODO: __REPLACE_CODE

            /* ... */
        }
    }


    public static final class UserNoticeViewHolder extends AbstractTwitchRecyclerViewHolder {
        private volatile List<GlideChatImageCustomTarget> loadedImageTargets; // TODO: __INJECT_FIELD

        /* ... */

        public UserNoticeViewHolder(View itemView) {
            super(itemView);
            /* ... */

            throw new VirtualImpl();
        }

        public final TextView getSystemMessage() {
            throw new VirtualImpl();
        }

        public final TextView getChatMessage() {
            throw new VirtualImpl();
        }

        public final void addTargetsToClear(List<GlideChatImageCustomTarget> targets) { // TODO: __INJECT_METHOD
            if (loadedImageTargets == null) {
                synchronized (this) {
                    if (loadedImageTargets == null) {
                        loadedImageTargets = new ArrayList<>(targets);
                        return;
                    }
                }
            }
            loadedImageTargets.addAll(targets);
        }

        @Override
        public void onRecycled() { // TODO: __INJECT_METHOD
            List<? extends GlideChatImageCustomTarget> targets = this.loadedImageTargets;
            if (targets != null) {
                for (GlideChatImageCustomTarget target : targets) {
                    Context applicationContext = this.itemView.getContext().getApplicationContext();
                    GlideHelper.INSTANCE.clearPendingGlideLoad(applicationContext, target);
                }
            }
        }

        /* ... */
    }

    /* ... */
}

package tv.twitch.android.shared.chat.emotecard;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import tv.twitch.android.core.mvp.viewdelegate.RxViewDelegate;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.shared.ui.elements.image.SquareNetworkImageWidget;

public class EmoteCardViewDelegate extends RxViewDelegate {
    private final LinearLayout channelButton = null;
    private final TextView channelLiveIndicator = null;
    private final TextView channelName = null;
    private final ImageView emoteAnimationIndicatorIcon = null;
    private final ConstraintLayout emoteCardContent = null;
    private final TextView emoteDesc = null;
    private final SquareNetworkImageWidget emoteImage = null;
    private final TextView emoteName = null;
    private final FrameLayout errorContainer = null;
    private final LinearLayout followButton = null;
    private final TextView followButtonText = null;
    private final ImageView followIcon = null;
    private final ProgressBar loadingIndicator = null;
    private final RecyclerView relatedEmotes = null;
    private final EmoteCardRelatedEmotesAdapterBinder relatedEmotesAdapterBinder = null;
    private final LinearLayout reportButton = null;
    private final LinearLayout subscribeButton = null;
    private final TextView subscribeButtonText = null;
    private final ImageView subscribeIcon = null;
    private final TextView usageNotice = null;

    public EmoteCardViewDelegate(View view) {
        super(view);
    }


    private final void renderEmoteCard(EmoteCardState.Loaded p0) {
//        View[] viewArray;
//        View[] viewArray1;
//        EmoteCardUiModel emoteCardUiM = p0.getEmoteCardUiModel();
//        EmoteCardHeaderUiModel header = emoteCardUiM.getHeader();
//        this.emoteName.setText(header.getEmoteName());
//        this.emoteDesc.setText(header.getEmoteType().getString(this.getContext()));
//        NetworkImageWidget.setImageURL$default(this.emoteImage, header.getEmoteUrl(), false, 0, null, false, 30, null);
//        ViewExtensionsKt.visibilityForBoolean(this.emoteAnimationIndicatorIcon, header.isEmoteAnimationIconVisible());
//        if (NullableUtils.ifNotNull(emoteCardUiM.getDescription(), new EmoteCardViewDelegate$renderEmoteCard$1(this)) == null) {
//            viewArray = new View[1];
//            viewArray[0] = this.usageNotice;
//            this.hideViews(viewArray);
//        }
//        if (NullableUtils.ifNotNull(emoteCardUiM.getRelatedEmotes(), new EmoteCardViewDelegate$renderEmoteCard$2(this)) == null) {
//            viewArray = new View[1];
//            viewArray[0] = this.relatedEmotes;
//            this.hideViews(viewArray);
//        }
//        if (NullableUtils.ifNotNull(emoteCardUiM.getChannelButton(), new EmoteCardViewDelegate$renderEmoteCard$3(this)) == null) {
//            viewArray = new View[1];
//            viewArray[0] = this.channelButton;
//            this.hideViews(viewArray);
//        }
//        if (NullableUtils.ifNotNull(emoteCardUiM.getFollowButton(), new EmoteCardViewDelegate$renderEmoteCard$4(this)) == null) {
//            viewArray = new View[1];
//            viewArray[0] = this.followButton;
//            this.hideViews(viewArray);
//        }
//        if (NullableUtils.ifNotNull(emoteCardUiM.getSubscribeButton(), new EmoteCardViewDelegate$renderEmoteCard$5(this)) == null) {
//            viewArray = new View[1];
//            viewArray[0] = this.subscribeButton;
//            this.hideViews(viewArray);
//        }
//        if (NullableUtils.ifNotNull(emoteCardUiM.getReportButton(), new EmoteCardViewDelegate$renderEmoteCard$6(this)) == null) {
//            viewArray1 = new View[1];
//            viewArray1[0] = this.reportButton;
//            this.hideViews(viewArray1);
//        }
        return;
    }

    @Override
    public void render(ViewDelegateState viewDelegateState) {

    }

    @Override
    public void pushEvent(Object o) {

    }
}

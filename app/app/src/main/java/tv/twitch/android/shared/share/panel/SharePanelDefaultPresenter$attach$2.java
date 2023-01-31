package tv.twitch.android.shared.share.panel;

import kotlin.Pair;
import tv.orange.features.vodhunter.Vodhunter;

public class SharePanelDefaultPresenter$attach$2 {
    final SharePanelDefaultPresenter this$0 = null;

    public final void invoke(Pair<? extends SharePanelDefaultPresenter.State, ? extends SharePanelWidgetViewDelegate.Event> pair) {
        SharePanelViewFactory sharePanelViewFactory;
        SharePanelDefaultPresenter.State state = pair.component1();
        SharePanelWidgetViewDelegate.Event event = pair.component2();

        if (state instanceof SharePanelDefaultPresenter.State.Initialized) {
            if (event instanceof SharePanelWidgetViewDelegate.Event.DownloadClicked) { // TODO: __INJECT_CODE
                Vodhunter.get().tryDownloadClip(this$0, (SharePanelDefaultPresenter.State.Initialized) state);
                return;
            }
        }
    }
}
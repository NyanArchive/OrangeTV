package tv.twitch.android.feature.followed;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.models.recommendationfeedback.RecommendationInfo;
import tv.twitch.android.models.streams.StreamModelContainer;

public class FollowedListAdapterBinder {
    private boolean showingTabletUI;

    /* ... */

    private final void bindStreams(List<? extends StreamModelContainer> list, Function1<? super RecommendationInfo, Unit> function1) {
        showingTabletUI = showingTabletUI || UI.getShowFullCards(); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }
}

package tv.twitch.android.shared.ui.elements.list;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContentListViewDelegate {
    private RecyclerView.Adapter<?> adapter;
    private GridLayoutManager layoutManager;
    private RecyclerView gridView;

    public final RecyclerView.Adapter<?> getAdapter() { // TODO: __INJECT_CODE
        return adapter;
    }

    public final void fastScrollToPosition(int i) { // TODO: __INJECT_CODE
        if (layoutManager != null) {
            if (gridView != null) {
                gridView.stopScroll();
            }
            layoutManager.scrollToPositionWithOffset(i, 0);
        }
    }
}

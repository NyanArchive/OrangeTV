package tv.twitch.android.feature.theatre.chomments;

import android.view.View;

import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.viewdelegate.BaseViewDelegate;

public class ChommentsHeaderViewDelegate extends BaseViewDelegate {
    /* ... */

    public ChommentsHeaderViewDelegate(View view) {
        super(view);

        /* ... */

        throw new VirtualImpl();
    }

    /* ... */

    @Override
    public void show() { // TODO: __INJECT_METHOD
        if (!UI.getHideChatHeader()) {
            super.show();
        }
    }
}

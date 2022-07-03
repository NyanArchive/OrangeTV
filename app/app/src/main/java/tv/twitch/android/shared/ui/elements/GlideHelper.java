package tv.twitch.android.shared.ui.elements;

import android.content.Context;
import android.widget.TextView;

import tv.twitch.android.shared.ui.elements.span.GlideChatImageCustomTarget;
import tv.twitch.android.shared.ui.elements.span.GlideChatImageTarget;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public final class GlideHelper {
    private static final GlideChatImageCustomTarget loadImageForUrlDrawableAngGetTarget(Context context, UrlDrawable urlDrawable, TextView textView) {
        GlideChatImageCustomTarget into = null;

        into.setContainer(textView); // TODO: __INJECT_CODE

        return null;
    }

    private static final void loadImageForUrlDrawable(Context context, UrlDrawable urlDrawable, TextView textView) {
        GlideChatImageTarget into = null;

        into.setContainer(textView); // TODO: __INJECT_CODE
    }
}

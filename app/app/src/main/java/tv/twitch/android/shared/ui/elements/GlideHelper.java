package tv.twitch.android.shared.ui.elements;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tv.orange.features.chat.bridge.StackUrlDrawable;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan;
import tv.twitch.android.shared.ui.elements.span.GlideChatImageCustomTarget;
import tv.twitch.android.shared.ui.elements.span.GlideChatImageTarget;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public final class GlideHelper {
    /* ... */

    private static GlideChatImageCustomTarget loadImageForUrlDrawableAngGetTarget(Context context, UrlDrawable urlDrawable, TextView textView) {
        GlideChatImageCustomTarget into = null;

        /* ... */

        into.setContainer(textView); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    private static void loadImageForUrlDrawable(Context context, UrlDrawable urlDrawable, TextView textView) {
        GlideChatImageTarget into = null;

        /* ... */

        into.setContainer(textView); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public static final void loadImagesFromSpanned(Context context, Spanned spanned, TextView textView) {
        Object[] spans = spanned.getSpans(0, spanned.length(), CenteredImageSpan.class);
        for (Object obj : spans) {
            Drawable imageDrawable = ((CenteredImageSpan) obj).getImageDrawable();
            if (imageDrawable instanceof StackUrlDrawable) { // TODO: __INJECT_CODE
                loadImagesForStackUrlDrawable(context, (StackUrlDrawable) imageDrawable, textView);
            }
        }

        /* ... */

        throw new VirtualImpl();
    }

    public static final List<GlideChatImageCustomTarget> loadImagesFromSpannedAndGetTargets(Context context, Spanned spanned, TextView textView) {
        List<GlideChatImageCustomTarget> emptyList;
        CenteredImageSpan[] spans = (CenteredImageSpan[]) spanned.getSpans(0, spanned.length(), CenteredImageSpan.class);
        ArrayList<GlideChatImageCustomTarget> arrayList = new ArrayList();
        for (CenteredImageSpan centeredImageSpan : spans) {
            Drawable imageDrawable = centeredImageSpan.getImageDrawable();
            if (imageDrawable instanceof StackUrlDrawable) { // TODO: __INJECT_CODE
                loadImagesForStackUrlDrawable(context, (StackUrlDrawable) imageDrawable, textView, arrayList);
            }
        }

        /* ... */

        throw new VirtualImpl();
    }

    private static void loadImagesForStackUrlDrawable(Context context, StackUrlDrawable imageDrawable, TextView textView, ArrayList<GlideChatImageCustomTarget> arrayList) { // TODO: __INJECT_METHOD
        arrayList.add(loadImageForUrlDrawableAngGetTarget(context, imageDrawable, textView));

        for (UrlDrawable drawable : imageDrawable.getStack()) {
            arrayList.add(loadImageForUrlDrawableAngGetTarget(context, drawable, textView));
        }
    }

    private static void loadImagesForStackUrlDrawable(Context context, StackUrlDrawable imageDrawable, TextView textView) { // TODO: __INJECT_METHOD
        loadImageForUrlDrawable(context, imageDrawable, textView);

        for (UrlDrawable drawable : imageDrawable.getStack()) {
            loadImageForUrlDrawable(context, drawable, textView);
        }
    }

    /* ... */
}

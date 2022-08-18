package tv.twitch.android.shared.ui.elements;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.app.core.ActivityUtilKt;
import tv.twitch.android.shared.ui.elements.span.CenteredImageSpan;
import tv.twitch.android.shared.ui.elements.span.FixedTimeCoordinatorDrawable;
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

    public static final void loadImagesFromSpanned(Context context, Spanned spanned, TextView textView) { // TODO: __REPLACE_METHOD
        if (ActivityUtilKt.isInvalid(context)) {
            return;
        }
        Object[] spans = spanned.getSpans(0, spanned.length(), CenteredImageSpan.class);
        for (Object obj : spans) {
            Drawable imageDrawable = ((CenteredImageSpan) obj).getImageDrawable();
            if (imageDrawable instanceof FixedTimeCoordinatorDrawable) {
                FixedTimeCoordinatorDrawable fixedTimeCoordinatorDrawable = (FixedTimeCoordinatorDrawable) imageDrawable;
                if (fixedTimeCoordinatorDrawable.shouldDisplayInitialDrawable()) {
                    loadImagesForUrlDrawable(context, fixedTimeCoordinatorDrawable.getInitialUrlDrawable(), textView);
                }
                loadImagesForUrlDrawable(context, fixedTimeCoordinatorDrawable.getFinalUrlDrawable(), textView);
            } else if (imageDrawable instanceof UrlDrawable) {
                loadImagesForUrlDrawable(context, (UrlDrawable) imageDrawable, textView);
            }
        }
    }

    public static final List<GlideChatImageCustomTarget> loadImagesFromSpannedAndGetTargets(Context context, Spanned spanned, TextView textView) { // TODO: __REPLACE_METHOD
        if (ActivityUtilKt.isInvalid(context)) {
            return new ArrayList<>();
        }
        CenteredImageSpan[] spans = (CenteredImageSpan[]) spanned.getSpans(0, spanned.length(), CenteredImageSpan.class);
        ArrayList<GlideChatImageCustomTarget> arrayList = new ArrayList<>();
        for (CenteredImageSpan centeredImageSpan : spans) {
            Drawable imageDrawable = centeredImageSpan.getImageDrawable();
            if (imageDrawable instanceof FixedTimeCoordinatorDrawable) {
                FixedTimeCoordinatorDrawable fixedTimeCoordinatorDrawable = (FixedTimeCoordinatorDrawable) imageDrawable;
                if (fixedTimeCoordinatorDrawable.shouldDisplayInitialDrawable()) {
                    arrayList.addAll(loadImagesForUrlDrawableAngGetTarget(context, fixedTimeCoordinatorDrawable.getInitialUrlDrawable(), textView));
                }
                arrayList.addAll(loadImagesForUrlDrawableAngGetTarget(context, fixedTimeCoordinatorDrawable.getFinalUrlDrawable(), textView));
            } else if (imageDrawable instanceof UrlDrawable) {
                arrayList.addAll(loadImagesForUrlDrawableAngGetTarget(context, (UrlDrawable) imageDrawable, textView));
            }
        }
        return arrayList;
    }

    private static void loadImagesForUrlDrawable(Context context, UrlDrawable urlDrawable, TextView textView) { // TODO: __INJECT_METHOD
        loadImageForUrlDrawable(context, urlDrawable, textView);

        for (UrlDrawable drawable : urlDrawable.getStack()) {
            loadImagesForUrlDrawable(context, drawable, textView);
        }
    }

    private static List<GlideChatImageCustomTarget> loadImagesForUrlDrawableAngGetTarget(Context context, UrlDrawable urlDrawable, TextView textView) { // TODO: __INJECT_METHOD
        ArrayList<GlideChatImageCustomTarget> arrayList = new ArrayList<>();
        arrayList.add(loadImageForUrlDrawableAngGetTarget(context, urlDrawable, textView));

        for (UrlDrawable drawable : urlDrawable.getStack()) {
            arrayList.addAll(loadImagesForUrlDrawableAngGetTarget(context, drawable, textView));
        }

        return arrayList;
    }

    /* ... */
}

package tv.twitch.android.shared.ui.elements.span;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;

import tv.orange.models.exception.VirtualImpl;

public class GlideChatImageTarget {
    private UrlDrawable mUrlDrawable;
    private WeakReference<View> mContainer; // TODO: __INJECT_FIELD

    /* ... */

    public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
        /* ... */

        maybeInvalidateContainer(drawable); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    private Point scaleSquared(float f2, float f3, float f4) { // TODO: __REPLACE_METHOD
        float f5 = 1.0f;
        float f6 = f2 > f4 ? f4 / f2 : 1.0f;
        if (f3 > f4) {
            f5 = f4 / f3;
        }
        UrlDrawable drawable = mUrlDrawable;
        float min = f5;
        if (drawable == null || !drawable.isWide()) {
            min = Math.min(f6, f5);
        }

        return new Point((int) (f2 * min), (int) (f3 * min));
    }

    /* ... */

    public void maybeInvalidateContainer(Drawable drawable) { // TODO: __INJECT_METHOD
        if (drawable == null) {
            return;
        }

        Rect bounds = drawable.getBounds();
        if (bounds.right <= bounds.bottom) {
            return;
        }

        WeakReference<View> ref = mContainer;
        if (ref != null) {
            View view = ref.get();
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setText(tv.getText(), TextView.BufferType.SPANNABLE);
            }
            ref.clear();
        }
    }

    public void setContainer(View view) { // TODO: __INJECT_METHOD
        synchronized (this) {
            if (mContainer != null) {
                mContainer.clear();
            }

            if (view != null) {
                mContainer = new WeakReference<>(view);
            } else {
                mContainer = null;
            }
        }
    }
}

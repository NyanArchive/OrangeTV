package tv.twitch.android.shared.ui.elements.span;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import tv.twitch.android.app.core.Utility;

@SuppressWarnings("deprecation")
public class UrlDrawable extends BitmapDrawable { // TODO: __REPLACE_CLASS
    private Drawable drawable;
    private boolean isDestroyed;
    private Function1<? super Rect, Unit> onBoundsChangeListener;
    private final MediaSpan$Type type;
    private final String url;
    private final boolean wide;

    private boolean grey = false;
    private ColorMatrix greyMatrix;
    private ColorMatrixColorFilter greyFilter;

    private final List<UrlDrawable> stack = new ArrayList<>();

    public List<UrlDrawable> getStack() {
        return stack;
    }

    public void addToStack(UrlDrawable drawable) {
        stack.add(drawable);
    }

    public void addToStack(List<UrlDrawable> drawables) {
        stack.addAll(drawables);
    }

    public UrlDrawable() {
        this(null, null, 3, null);
    }

    public String getUrl() {
        return this.url;
    }

    public UrlDrawable(String str, MediaSpan$Type mediaSpan$Type, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "" : str, (i & 2) != 0 ? MediaSpan$Type.Emote : mediaSpan$Type, false);
    }

    public UrlDrawable(String url, MediaSpan$Type type) {
        this(url, type, false);
    }

    public boolean isWide() {
        return wide;
    }

    public UrlDrawable(String url, MediaSpan$Type type, boolean wide) {
        this.url = url;
        this.type = type;
        this.wide = wide;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setOnBoundsChangeListener(Function1<? super Rect, Unit> listener) {
        this.onBoundsChangeListener = listener;
    }

    public int getImageSize() {
        return (int) Utility.dpToPixels(this.type.getSizeDp());
    }

    public boolean isDestroyed() {
        return this.isDestroyed;
    }

    public boolean isReady() {
        return this.drawable != null;
    }

    public void setGrey(boolean state) {
        grey = state;
    }

    @Override
    public void draw(Canvas canvas) {
        Drawable drawable = this.drawable;
        if (drawable != null) {
            if (grey) {
                setGreyFilter(drawable);
            }
            drawable.draw(canvas);
            startAnimation(drawable);
        }

        for (UrlDrawable stack : getStack()) {
            stack.draw(canvas);
        }
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        Function1<? super Rect, Unit> function1 = this.onBoundsChangeListener;
        if (function1 != null) {
            function1.invoke(rect);
        }
    }

    public void destroy() {
        Collections.reverse(stack);

        for (UrlDrawable stack : getStack()) {
            stack.destroy();
        }

        isDestroyed = true;
        stopAnimation(drawable);
        drawable = null;
        onBoundsChangeListener = null;
        greyMatrix = null;
        greyFilter = null;
    }

    private void setGreyFilter(Drawable drawable) {
        if (greyMatrix == null) {
            greyMatrix = new ColorMatrix();
            greyMatrix.setSaturation(0);
        }
        if (greyFilter == null) {
            greyFilter = new ColorMatrixColorFilter(greyMatrix);
        }

        drawable.setColorFilter(greyFilter);
    }

    private static void startAnimation(Drawable drawable) {
        if (drawable instanceof GifDrawable) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
            if (!gifDrawable.isRunning()) {
                gifDrawable.start();
            }
        } else if (drawable instanceof WebpDrawable) {
            WebpDrawable webpDrawable = (WebpDrawable) drawable;
            if (!webpDrawable.isRunning()) {
                webpDrawable.start();
            }
        }
    }

    private static void stopAnimation(Drawable drawable) {
        if (drawable instanceof GifDrawable) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
            if (gifDrawable.isRunning()) {
                gifDrawable.stop();
            }
        } else if (drawable instanceof WebpDrawable) {
            WebpDrawable webpDrawable = (WebpDrawable) drawable;
            if (webpDrawable.isRunning()) {
                webpDrawable.stop();
            }
        }
    }
}

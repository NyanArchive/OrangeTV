package tv.twitch.android.shared.ui.elements.span;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import tv.twitch.android.app.core.Utility;

public final class UrlDrawable extends BitmapDrawable {
    private Drawable drawable;
    private boolean isDestroyed;
    private Function1<? super Rect, Unit> onBoundsChangeListener;
    private final MediaSpan$Type type;
    private final String url;

    public String getUrl() {
        return this.url;
    }

    public UrlDrawable(String url, MediaSpan$Type type) {
        this.url = url;
        this.type = type;
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

    @Override
    public void draw(Canvas canvas) { // TODO: __REPLACE_METHOD
        Drawable drawable = this.drawable;
        if (drawable != null) {
            drawable.draw(canvas);
            if (drawable instanceof GifDrawable) {
                GifDrawable gifDrawable = (GifDrawable) drawable;
                if (!gifDrawable.isRunning()) {
                    gifDrawable.start();
                }
            } else if (drawable instanceof WebpDrawable) {
                WebpDrawable gifDrawable = (WebpDrawable) drawable;
                if (!gifDrawable.isRunning()) {
                    gifDrawable.start();
                }
            }
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

    public void destroy() { // TODO: __REPLACE_METHOD
        this.isDestroyed = true;
        Drawable drawable = this.drawable;
        if (drawable instanceof GifDrawable) {
            GifDrawable gifDrawable = (GifDrawable) drawable;
            if (gifDrawable.isRunning()) {
                gifDrawable.stop();
            }
        } else if (drawable instanceof WebpDrawable) {
            WebpDrawable gifDrawable = (WebpDrawable) drawable;
            if (gifDrawable.isRunning()) {
                gifDrawable.stop();
            }
        }
        this.drawable = null;
        this.onBoundsChangeListener = null;
    }
}
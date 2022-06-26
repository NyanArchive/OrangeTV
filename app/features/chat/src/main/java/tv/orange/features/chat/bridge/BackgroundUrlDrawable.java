package tv.orange.features.chat.bridge;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import tv.twitch.android.shared.ui.elements.span.MediaSpan$Type;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public class BackgroundUrlDrawable extends UrlDrawable {
    private final int backgroundColor;

    public BackgroundUrlDrawable(String url, MediaSpan$Type type, int backgroundColor) {
        super(url, type);
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setDrawable(Drawable drawable) {
        if (backgroundColor != Color.TRANSPARENT) {
            if (drawable != null) {
                drawable.setColorFilter(backgroundColor, PorterDuff.Mode.DST_OVER);
            }
        }

        super.setDrawable(drawable);
    }
}

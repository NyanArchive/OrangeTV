package tv.orange.features.chat.bridge;

import android.graphics.Canvas;

import java.util.Collections;
import java.util.List;

import tv.twitch.android.shared.ui.elements.span.MediaSpan$Type;
import tv.twitch.android.shared.ui.elements.span.UrlDrawable;

public class StackUrlDrawable extends UrlDrawable {
    private final List<UrlDrawable> stack;

    public StackUrlDrawable(String url, MediaSpan$Type type, List<UrlDrawable> drawables) {
        super(url, type, true);
        this.stack = drawables;
    }

    public List<UrlDrawable> getStack() {
        return stack;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (UrlDrawable drawable : stack) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }

    @Override
    public void destroy() {
        Collections.reverse(stack);
        for (UrlDrawable drawable : stack) {
            if (drawable != null) {
                drawable.destroy();
            }
        }
        super.destroy();
    }
}

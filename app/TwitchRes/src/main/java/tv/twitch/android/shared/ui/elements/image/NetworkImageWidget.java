package tv.twitch.android.shared.ui.elements.image;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class NetworkImageWidget extends AppCompatImageView {
    public NetworkImageWidget(@NonNull Context context) {
        super(context);
    }

    public NetworkImageWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

package tv.twitch.android.shared.ui.elements.image;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AspectRatioMaintainingNetworkImageWidget extends NetworkImageWidget {

    public AspectRatioMaintainingNetworkImageWidget(@NonNull Context context) {
        super(context);
    }

    public AspectRatioMaintainingNetworkImageWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioMaintainingNetworkImageWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

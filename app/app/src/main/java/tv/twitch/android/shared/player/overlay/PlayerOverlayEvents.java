package tv.twitch.android.shared.player.overlay;

import kotlin.jvm.internal.DefaultConstructorMarker;

public abstract class PlayerOverlayEvents {
    public PlayerOverlayEvents(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    private PlayerOverlayEvents() {
    }

    public static final class Refresh extends PlayerOverlayEvents {
        public static final Refresh INSTANCE = new Refresh();

        private Refresh() {
            super(null);
        }
    }

    public static final class ChangePlaybackSpeed extends PlayerOverlayEvents { // TODO: __INJECT_CLASS
        private final float speed;

        public ChangePlaybackSpeed(float speed) {
            super(null);
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }
    }
}

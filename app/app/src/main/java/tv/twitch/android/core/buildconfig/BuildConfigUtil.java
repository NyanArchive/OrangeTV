package tv.twitch.android.core.buildconfig;

import tv.orange.core.Hook;

public class BuildConfigUtil {
    public final boolean isDebugConfigEnabled() { // TODO: __REPLACE_METHOD
        return Hook.get().inDevMode();
    }

    public final boolean isAlpha() { // TODO: __REPLACE_METHOD
        return Hook.get().inDevMode();
    }

    public final boolean isBeta() { // TODO: __REPLACE_METHOD
        return Hook.get().inDevMode();
    }

    public final boolean shouldShowDebugOptions(boolean z) { // TODO: __REPLACE_METHOD
        return Hook.get().inDevMode();
    }

    public final boolean bugReportingEnabled() { // TODO: __REPLACE_METHOD
        return Hook.get().inDevMode();
    }
}

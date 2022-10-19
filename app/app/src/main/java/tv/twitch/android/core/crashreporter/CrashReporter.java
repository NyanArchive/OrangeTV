package tv.twitch.android.core.crashreporter;

import android.app.Application;

import java.util.Arrays;

import tv.orange.features.tracking.SentrySDK;
import tv.twitch.android.util.LogArg;
import tv.twitch.android.util.LogTag;

public class CrashReporter {
    private static volatile Application application;

    /* ... */

    public enum LogLevel {
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        ASSERT(7);

        private final int value;

        LogLevel(int i) {
            this.value = i;
        }

        public final int getValue() {
            return this.value;
        }
    }

    /* ... */

    public final void log(LogLevel logLevel, LogTag tag, int i, LogArg... args) { // TODO: __REPLACE_METHOD
        String safeLogMessage = CrashReporterContextKt.getSafeLogMessage(application, i, (LogArg[]) Arrays.copyOf(args, args.length));
        SentrySDK.INSTANCE.logEvent(logLevel.name(), tag.value + ": " + safeLogMessage);
    }

    public final void logException(Throwable throwable) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.logException(throwable, "CrashReporter");
    }

    public final void setBool(String key, boolean z) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.setBool(key, z);
    }

    public final void setString(String key, String str) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.setTag(key, str);
    }

    public final void setInteger(String key, int i) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.setInteger(key, i);
    }

    public final void setLong(String key, long j) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.setLong(key, j);
    }

    public final void setUserNameForDebugBuilds(String str) { // TODO: __REPLACE_METHOD
        SentrySDK.INSTANCE.setUser(str);
    }

    public final void setUserIdentifierForDebugBuilds(String str) {// TODO: __REPLACE_METHOD
    }

    /* ... */
}

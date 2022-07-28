package tv.twitch.android.core.crashreporter;

public class CrashReporter {
    /* ... */

    public final void logException(Throwable throwable) { // TODO: __REPLACE_METHOD
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    /* ... */
}

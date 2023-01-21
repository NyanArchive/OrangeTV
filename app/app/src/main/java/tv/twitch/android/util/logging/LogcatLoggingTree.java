package tv.twitch.android.util.logging;

import android.util.Log;

import tv.orange.core.LoggerImpl;

public class LogcatLoggingTree {
    /* ... */

    protected void log(int i, String str, String message, Throwable th) { // TODO: __REPLACE_CLASS
        if (th != null) {
            LoggerImpl.INSTANCE.devDebug(message + "--->" + Log.getStackTraceString(th));
            return;
        }
        LoggerImpl.INSTANCE.devDebug(message);
    }

    /* ... */
}

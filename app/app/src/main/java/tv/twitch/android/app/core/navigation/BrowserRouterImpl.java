package tv.twitch.android.app.core.navigation;

import android.net.Uri;

import androidx.fragment.app.FragmentActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import tv.orange.features.ui.UI;
import tv.orange.models.exception.VirtualImpl;

public class BrowserRouterImpl {
    /* ... */

    private final void showTwitchDialogForBrowserWithUri(FragmentActivity fragmentActivity, Uri uri, Function0<Unit> function0, boolean z) {
        if (UI.get().getSkipTwitchBrowserDialog()) { // TODO: __INJECT_CODE
            queryPackageManagerForBrowserIntent(fragmentActivity, uri, function0, true);
            return;
        }

        throw new VirtualImpl();
    }

    private final void queryPackageManagerForBrowserIntent(FragmentActivity fragmentActivity, Uri uri, Function0<Unit> function0, boolean z) {
        throw new VirtualImpl();
    }

    /* ... */
}

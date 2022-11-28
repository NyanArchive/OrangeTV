package tv.twitch.android.shared.chat.observables;

import io.reactivex.functions.Predicate;
import tv.orange.features.chat.ChatHookProvider;
import tv.orange.models.exception.VirtualImpl;

public class ChatConnectionController$$ExternalSyntheticLambda2 implements Predicate {
    @Override
    public boolean test(Object o) throws Exception {
        if (ChatHookProvider.isChatKilled()) { // TODO: __INJECT_CODE
            return false;
        }

        /* ... */

        throw new VirtualImpl();
    }
}

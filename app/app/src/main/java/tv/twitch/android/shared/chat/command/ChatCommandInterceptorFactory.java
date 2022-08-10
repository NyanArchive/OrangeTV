package tv.twitch.android.shared.chat.command;

import java.util.Set;

import tv.orange.features.spam.Spam;
import tv.orange.models.exception.VirtualImpl;

public class ChatCommandInterceptorFactory {
    private final VoteCommandInterceptor voteCommandInterceptor;
    private final ChatCommandInterceptor spamCommandInterceptor; // TODO: __INJECT_FIELD

    /* ... */

    public ChatCommandInterceptorFactory() {
        voteCommandInterceptor = null;

        /* ... */

        spamCommandInterceptor = Spam.get().createSpamCommandInterceptor(voteCommandInterceptor); // TODO: __INJECT_CODE

        throw new VirtualImpl();
    }

    /* ... */

    public final Set<ChatCommandInterceptor> getInterceptors() {
        return null; // TODO: __INJECT_SPAM_TO_SET
    }
}

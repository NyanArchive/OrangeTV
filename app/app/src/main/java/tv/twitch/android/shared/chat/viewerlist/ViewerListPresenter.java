package tv.twitch.android.shared.chat.viewerlist;

import java.util.Map;

import kotlin.jvm.internal.DefaultConstructorMarker;
import tv.orange.features.usersearch.UserSearch;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.PresenterAction;
import tv.twitch.android.core.mvp.presenter.PresenterState;
import tv.twitch.android.core.mvp.presenter.RxPresenter;
import tv.twitch.android.core.mvp.presenter.StateAndAction;
import tv.twitch.android.core.mvp.presenter.StateMachine;
import tv.twitch.android.core.mvp.presenter.StateMachineKt;
import tv.twitch.android.core.mvp.presenter.StateUpdateEvent;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.models.chat.Chatters;
import tv.twitch.android.shared.api.pub.BadgeKey;

public class ViewerListPresenter extends RxPresenter { // TODO: @features:usersearch
    private final StateMachine stateMachine = null;

    /* ... */

    @Override
    public void pushState(Object o) {
        /* ... */

        throw new VirtualImpl();
    }

    public void attach(ViewerListViewDelegate viewDelegate) {
        /* ... */

        UserSearch.get().setupFilter(viewDelegate, this); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public StateAndAction<State, PresenterAction> getStateSearchText(State state, UpdateEvent.SearchTextUpdateEvent updateEvent) { // TODO: __INJECT_CLASS
        if (state instanceof State.Open) {
            State.Open open = (State.Open) state;
            state = open.copy(open.getChannelName(), UserSearch.get().filterChatters(open.getChatters(), updateEvent.getText()), open.getBadges(), open.getSectionExpandedStates(), false);
        }
        return StateMachineKt.noAction(state);
    }

    /* ... */

    public static abstract class UpdateEvent implements StateUpdateEvent { // TODO: __PATCH_CLASS
        public UpdateEvent(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private UpdateEvent() {
        }

        public static final class SearchTextUpdateEvent extends UpdateEvent { // TODO: __INJECT_CLASS
            private final String text;

            public SearchTextUpdateEvent(String s) {
                text = s;
            }

            public String getText() {
                return text;
            }
        }
    }

    public static abstract class State implements PresenterState, ViewDelegateState {
        /* ... */

        public static final class Open extends State {
            private final Map<BadgeKey, String> badges;
            private final String channelName;
            private final Chatters chatters;
            private final boolean hasError;
            private final Map<ViewerListUserGroup, Boolean> sectionExpandedStates;

            public final Open copy(String channelName, Chatters chatters, Map<BadgeKey, String> map, Map<ViewerListUserGroup, Boolean> sectionExpandedStates, boolean z) {
                return new Open(channelName, chatters, map, sectionExpandedStates, z);
            }

            public String toString() {
                return "Open(channelName=" + this.channelName + ", chatters=" + this.chatters + ", badges=" + this.badges + ", sectionExpandedStates=" + this.sectionExpandedStates + ", hasError=" + this.hasError + ')';
            }

            public final String getChannelName() {
                return this.channelName;
            }

            public final Chatters getChatters() {
                return this.chatters;
            }

            public final Map<BadgeKey, String> getBadges() {
                return this.badges;
            }

            public  Open(String str, Chatters chatters, Map map, Map map2, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
                throw new VirtualImpl();
            }

            public final Map<ViewerListUserGroup, Boolean> getSectionExpandedStates() {
                return this.sectionExpandedStates;
            }

            public final boolean getHasError() {
                return this.hasError;
            }

            public Open(String channelName, Chatters chatters, Map<BadgeKey, String> map, Map<ViewerListUserGroup, Boolean> sectionExpandedStates, boolean z) {
                this.channelName = channelName;
                this.chatters = chatters;
                this.badges = map;
                this.sectionExpandedStates = sectionExpandedStates;
                this.hasError = z;
            }
        }

        /* ... */
    }

    public void pushSearchEvent(String text) { // TODO: __INJECT_METHOD
        stateMachine.pushEvent(new UpdateEvent.SearchTextUpdateEvent(text));
    }
}

package tv.twitch.android.shared.chat.viewerlist;

import android.content.Context;
import android.view.View;

import androidx.appcompat.widget.SearchView;

import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.DefaultConstructorMarker;
import tv.orange.features.usersearch.Hook;
import tv.orange.features.usersearch.bridge.IViewerListViewDelegate;
import tv.twitch.android.core.mvp.viewdelegate.RxViewDelegate;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateEvent;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.shared.ui.elements.list.ContentListViewDelegate;

public class ViewerListViewDelegate extends RxViewDelegate implements IViewerListViewDelegate {
    private final SearchView searchBar; // TODO: __INJECT_FIELD

    public ViewerListViewDelegate(Context context, View root, ContentListViewDelegate contentListViewDelegate) {
        super(root);

        /* ... */

        searchBar = Hook.INSTANCE.getSearchBar(this); // TODO: __INJECT_CODE
    }

    @Override
    public void onInputSearchTextChanged(@Nullable String text) { // TODO: __INJECT_METHOD
        pushEvent(new Event.Search(text));
    }

    @Override
    public void render(ViewDelegateState viewDelegateState) {
        /* ... */
    }

    @Override
    public void pushEvent(Object o) {
        /* ... */
    }

    public static abstract class Event implements ViewDelegateEvent {
        public Event(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static final class Dismiss extends Event {
            public static final Dismiss INSTANCE = new Dismiss();

            private Dismiss() {
                super(null);
            }
        }

        public static final class Search extends Event { // TODO: __INJECT_CLASS
            private final String text;

            public Search(String text) {
                super(null);
                this.text = text;
            }

            public String getText() {
                return text;
            }
        }

        private Event() {
        }
    }
}

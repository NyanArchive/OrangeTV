package tv.twitch.android.feature.theatre.debug;

import android.text.TextUtils;

import java.util.Map;

import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.core.mvp.presenter.PresenterAction;
import tv.twitch.android.core.mvp.presenter.PresenterState;
import tv.twitch.android.core.mvp.presenter.StateAndAction;
import tv.twitch.android.core.mvp.presenter.StateUpdateEvent;
import tv.twitch.android.core.mvp.viewdelegate.ViewDelegateState;
import tv.twitch.android.models.manifest.ManifestModel;

public class VideoDebugListPresenter {
    /* ... */

    public enum VideoStat { // TODO: __REPLACE_CLASS
        BITRATE_AVG("Avg Bitrate"),
        BITRATE_ESTIMATE("Est. Bitrate"),
        BUFFER_EMPTIES("Buffer Empties"),
        BUFFER_SIZE("Buffer Size"),
        CLUSTER("Cluster"),
        CODECS("Codecs"),
        DROPPED_FRAMES("Dropped Frames"),
        HAS_SURESTREAM("Surestream"),
        HLS_LATENCY("HLS Latency"),
        LOW_LATENCY("Low Latency"),
        MW_LOGGED("Minutes logged"),
        NODE("Node"),
        PLAYER("Player"),
        PROTOCOL("Protocol"),
        RESOLUTION("Resolution"),
        SELECTED_QUALITY("Quality"),
        SERVING_ID("Serving ID"),
        USER_PATH("User Path"),
        PROXY_SERVER("Proxy Server"); // TODO: __INJECT_FIELD

        private final String label;

        VideoStat(String str) {
            label = str;
        }

        public final String getLabel() {
            return label;
        }
    }

    private void updateProxyServerInfo(State state, Event.ManifestUpdated manifestUpdated) {  // TODO: __INJECT_METHOD
        String proxyServer = manifestUpdated.getManifest().getProxyServer();
        if (!TextUtils.isEmpty(proxyServer)) {
            state.getData().put(VideoStat.PROXY_SERVER, proxyServer);
        }
    }

    private final StateAndAction<VideoDebugListPresenter.State, VideoDebugListPresenter.Action> handleManifestUpdate(VideoDebugListPresenter.State state, VideoDebugListPresenter.Event.ManifestUpdated event) {
        updateProxyServerInfo(state, event); // TODO: __INJECT_CODE

        /* ... */

        throw new VirtualImpl();
    }

    public abstract static class Event implements StateUpdateEvent {
        /* ... */

        public static final class ManifestUpdated extends VideoDebugListPresenter.Event {
            /* ... */

            public ManifestModel getManifest() {
                throw new VirtualImpl();
            }

            /* ... */
        }

        /* ... */
    }

    public abstract static class State implements PresenterState, ViewDelegateState {
        private Map<VideoStat, String> data;

        /* ... */

        public final Map<VideoStat, String> getData() {
            return this.data;
        }

        /* ... */
    }

    public abstract static class Action implements PresenterAction {/* ... */
    }

    /* ... */
}

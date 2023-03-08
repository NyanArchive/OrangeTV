package com.google.android.exoplayer2;

import androidx.annotation.Nullable;

import tv.orange.models.exception.VirtualImpl;

public class ExoPlayerImpl {
    private PlaybackParameters playbackParameters;
    private int pendingSetPlaybackParametersAcks;
    private final ExoPlayerImplInternal internalPlayer = null;


    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) { // TODO: __INJECT_METHOD
        if (playbackParameters == null) {
            playbackParameters = PlaybackParameters.DEFAULT;
        }
        if (this.playbackParameters.equals(playbackParameters)) {
            return;
        }
        pendingSetPlaybackParametersAcks++;
        this.playbackParameters = playbackParameters;
        internalPlayer.setPlaybackParameters(playbackParameters);
        PlaybackParameters playbackParametersToNotify = playbackParameters;
        notifyListeners(listener -> listener.onPlaybackParametersChanged(playbackParametersToNotify));
    }

    private void notifyListeners(final BasePlayer.ListenerInvocation listenerInvocation) {
        throw new VirtualImpl();
    }
}

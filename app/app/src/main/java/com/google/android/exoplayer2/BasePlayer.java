package com.google.android.exoplayer2;

public abstract class BasePlayer {
    protected interface ListenerInvocation {
        void invokeListener(Player.EventListener eventListener);
    }

}

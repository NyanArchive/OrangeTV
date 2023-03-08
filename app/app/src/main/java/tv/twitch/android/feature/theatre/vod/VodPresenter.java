package tv.twitch.android.feature.theatre.vod;

import tv.twitch.android.shared.player.presenters.IVodPlayerPresenter;

public class VodPresenter {
    private final IVodPlayerPresenter vodPlayerPresenter = null;

    public void tryChangeSpeed(float speed) { // TODO: __INJECT_METHOD
        vodPlayerPresenter.tryChangeVodSpeed(speed);
    }
}

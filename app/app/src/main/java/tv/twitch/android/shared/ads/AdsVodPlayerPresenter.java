package tv.twitch.android.shared.ads;

import tv.twitch.android.shared.player.presenters.IVodPlayerPresenter;
import tv.twitch.android.shared.player.presenters.VodPlayerPresenter;

public class AdsVodPlayerPresenter implements IVodPlayerPresenter {
    private final VodPlayerPresenter vodPlayerPresenter = null;

    public void tryChangeVodSpeed(float speed) { // TODO: __INJECT_METHOD
        vodPlayerPresenter.tryChangeVodSpeed(speed);
    }
}

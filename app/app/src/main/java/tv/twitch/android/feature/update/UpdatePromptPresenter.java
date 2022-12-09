package tv.twitch.android.feature.update;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import tv.orange.core.LoggerImpl;
import tv.orange.features.updater.Updater;
import tv.twitch.android.core.mvp.presenter.BasePresenter;
import tv.twitch.android.util.Optional;

public class UpdatePromptPresenter extends BasePresenter {
    private Disposable disposable;
    private boolean madeOneAppUpdateInfoRequest;
    private boolean requestedDownloadedUpdateInstall;
    private BehaviorSubject<Optional<UpdatePromptPresenterListener>> listenerBehaviorSubject;

    /* ... */

    public interface LifecycleEventRegistrar {
        void registerForLifecycleEvents(BasePresenter basePresenter);
    }

    public interface UpdatePromptPresenterListener {
        void availableVersionMismatchInDebugConfigOnly(int i, int i2);

        void requestsUpdateFlow(UpdateFlowStarter updateFlowStarter);

        void updateDownloadPending();

        void updateDownloadStarted();

        void updateDownloadedAndReadyToInstall();
    }

    private final boolean canUseAppUpdateManager() {
        return true;
    }

    public final class UpdateFlowStarter {
        /* ... */
    }

    /* ... */

    public final void startObserving(LifecycleEventRegistrar lifecycleEventRegistrar) { // TODO: __REPLACE_METHOD
        if (canUseAppUpdateManager()) {
            lifecycleEventRegistrar.registerForLifecycleEvents(this);
            if (this.madeOneAppUpdateInfoRequest) {
                return;
            }
            this.madeOneAppUpdateInfoRequest = true;
            Updater.get().injectToUpdatePromptPresenter(this, listenerBehaviorSubject);
        }
    }

    public final boolean installUpdate() { // TODO: __REPLACE_METHOD
        return true;
    }

    /* ... */
}

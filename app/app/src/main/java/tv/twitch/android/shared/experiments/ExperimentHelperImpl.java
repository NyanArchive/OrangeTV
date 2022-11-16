package tv.twitch.android.shared.experiments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Set;

import io.reactivex.Observable;
import tv.orange.core.bridge.ExperimentHelperHookImpl;
import tv.orange.core.bridge.IExperimentHelper;
import tv.orange.models.exception.VirtualImpl;
import tv.twitch.android.provider.experiments.ChannelExperiment;
import tv.twitch.android.provider.experiments.Experiment;
import tv.twitch.android.provider.experiments.ExperimentHelper;
import tv.twitch.android.provider.experiments.MiniExperimentModel;
import tv.twitch.android.provider.experiments.RemoteConfigurable;

public class ExperimentHelperImpl implements ExperimentHelper, IExperimentHelper {
    private final ExperimentHelper hook = new ExperimentHelperHookImpl(this); // TODO: __INJECT_FIELD

    @NonNull
    @Override
    public String getGroupForExperimentOrg(@Nullable Experiment experiment) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @NonNull
    @Override
    public MiniExperimentModel getModelForExperimentIdOrg(@Nullable String str) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @NonNull
    @Override
    public String getTreatmentForExperimentIdOrg(@Nullable String str) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @NonNull
    @Override
    public Observable<Set<RemoteConfigurable>> getUpdatedRemoteConfigurablesObservableOrg() { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public boolean isFeatureFlagEnabledOrg(@NonNull RemoteConfigurable remoteConfigurable) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public boolean isInGroupForMultiVariantExperimentOrg(@NonNull Experiment experiment, @NonNull String str) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public boolean isInOnGroupForBinaryChannelExperimentOrg(@NonNull ChannelExperiment channelExperiment, @NonNull String str) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public boolean isInOnGroupForBinaryExperimentOrg(@NonNull Experiment experiment) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public boolean isInRestrictedLocaleForExperimentOrg(@NonNull Experiment experiment) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public void refreshExperimentsOrg(int i) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @Override
    public void refreshIfNeededOrg(int i) { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @NonNull
    @Override
    public Set<RemoteConfigurable> updateEnabledGroupsForActiveExperimentsOrg() { // TODO: __RENAME_METHOD
        throw new VirtualImpl();
    }

    @NonNull
    @Override
    public ExperimentHelper getHook() { // TODO: __INJECT_METHOD
        return hook;
    }

    @Override
    public String getGroupForExperiment(Experiment experiment) { // TODO: __INJECT_METHOD
        return hook.getGroupForExperiment(experiment);
    }

    @Override
    public MiniExperimentModel getModelForExperimentId(String s) { // TODO: __INJECT_METHOD
        return hook.getModelForExperimentId(s);
    }

    @Override
    public String getTreatmentForExperimentId(String s) { // TODO: __INJECT_METHOD
        return hook.getTreatmentForExperimentId(s);
    }

    @Override
    public Observable<Set<RemoteConfigurable>> getUpdatedRemoteConfigurablesObservable() { // TODO: __INJECT_METHOD
        return hook.getUpdatedRemoteConfigurablesObservable();
    }

    @Override
    public boolean isFeatureFlagEnabled(RemoteConfigurable remoteConfigurable) { // TODO: __INJECT_METHOD
        return hook.isFeatureFlagEnabled(remoteConfigurable);
    }

    @Override
    public boolean isInGroupForMultiVariantExperiment(Experiment experiment, String s) { // TODO: __INJECT_METHOD
        return hook.isInGroupForMultiVariantExperiment(experiment, s);
    }

    @Override
    public boolean isInOnGroupForBinaryChannelExperiment(ChannelExperiment channelExperiment, String s) { // TODO: __INJECT_METHOD
        return hook.isInOnGroupForBinaryChannelExperiment(channelExperiment, s);
    }

    @Override
    public boolean isInOnGroupForBinaryExperiment(Experiment experiment) { // TODO: __INJECT_METHOD
        return hook.isInOnGroupForBinaryExperiment(experiment);
    }

    @Override
    public boolean isInRestrictedLocaleForExperiment(Experiment experiment) { // TODO: __INJECT_METHOD
        return hook.isInRestrictedLocaleForExperiment(experiment);
    }

    @Override
    public void refreshExperiments(int i) { // TODO: __INJECT_METHOD
        hook.refreshExperiments(i);
    }

    @Override
    public void refreshIfNeeded(int i) { // TODO: __INJECT_METHOD
        hook.refreshIfNeeded(i);
    }

    @Override
    public Set<RemoteConfigurable> updateEnabledGroupsForActiveExperiments() { // TODO: __INJECT_METHOD
        return hook.updateEnabledGroupsForActiveExperiments();
    }
}

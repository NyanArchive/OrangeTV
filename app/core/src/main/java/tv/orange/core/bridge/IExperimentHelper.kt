package tv.orange.core.bridge

import io.reactivex.Observable
import tv.twitch.android.provider.experiments.*


interface IExperimentHelper {
    fun getGroupForExperimentOrg(experiment: Experiment?): String

    fun getModelForExperimentIdOrg(str: String?): MiniExperimentModel

    fun getTreatmentForExperimentIdOrg(str: String?): String

    fun getUpdatedRemoteConfigurablesObservableOrg(): Observable<MutableSet<RemoteConfigurable>>

    fun isFeatureFlagEnabledOrg(remoteConfigurable: RemoteConfigurable): Boolean

    fun isInGroupForMultiVariantExperimentOrg(experiment: Experiment, str: String): Boolean

    fun isInOnGroupForBinaryChannelExperimentOrg(
        channelExperiment: ChannelExperiment,
        str: String
    ): Boolean

    fun isInOnGroupForBinaryExperimentOrg(experiment: Experiment): Boolean

    fun isInRestrictedLocaleForExperimentOrg(experiment: Experiment): Boolean

    fun refreshExperimentsOrg(i: Int)

    fun refreshIfNeededOrg(i: Int)

    fun updateEnabledGroupsForActiveExperimentsOrg(): MutableSet<RemoteConfigurable>

    fun getHook(): ExperimentHelper
}
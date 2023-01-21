package tv.orange.core.bridge

import io.reactivex.Observable
import tv.orange.core.LoggerImpl
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import tv.twitch.android.provider.experiments.*

class ExperimentHelperHookImpl(private val org: IExperimentHelper) : ExperimentHelper {
    override fun getGroupForExperiment(p0: Experiment): String {
        return when (p0) {
            Experiment.ANIMATED_EMOTES -> ANIMATED_EMOTES_ACTIVE
            else -> org.getGroupForExperimentOrg(p0).also { res ->
                LoggerImpl.devDebug { "${p0.experimentName} --> $p0, res --> $res" }
            }
        }
    }

    override fun getTreatmentForExperimentId(p0: String): String {
        return org.getTreatmentForExperimentIdOrg(p0).also { res ->
            LoggerImpl.devDebug { "p0 --> $p0, res --> $res" }
        }
    }

    override fun isFeatureFlagEnabled(p0: RemoteConfigurable): Boolean {
        return org.isFeatureFlagEnabledOrg(p0).also { res ->
            LoggerImpl.devDebug { "${p0.displayName} --> $p0, res --> $res" }
        }
    }

    override fun isInGroupForMultiVariantExperiment(p0: Experiment, p1: String): Boolean {
        return when (p0) {
            Experiment.BILLING_UNAVAILABLE_DIALOG,
            Experiment.LATAM_CRONET,
            Experiment.LOAD_AD_PROPERTIES_ON_HOME_TAB,
            Experiment.HTTP3_WITH_CRONET_GLOBAL,
            Experiment.ANNUAL_RECAP_2022 -> false

            Experiment.FREEFORM_TAGS,
            Experiment.MULTI_OPTION_PREDICTIONS -> true

            Experiment.IMPROVED_BACKGROUND_AUDIO -> Flag.IMPROVED_BACKGROUND_AUDIO.asBoolean()

            else -> org.isInGroupForMultiVariantExperimentOrg(p0, p1).also { res ->
                LoggerImpl.devDebug { "${p0.experimentName} --> $p0, p1 --> $p1, res --> $res" }
            }
        }
    }

    override fun isInOnGroupForBinaryChannelExperiment(
        p0: ChannelExperiment,
        p1: String
    ): Boolean {
        return org.isInOnGroupForBinaryChannelExperimentOrg(p0, p1).also { res ->
            LoggerImpl.devDebug { "${p0.experimentName} --> $p0, p1 --> $p1, res --> $res" }
        }
    }

    override fun isInOnGroupForBinaryExperiment(p0: Experiment): Boolean {
        return when (p0) {
            Experiment.AUDIO_ADS,
            Experiment.DISABLE_AUDIO_ONLY,
            Experiment.ADS_SPONSORED_STREAMS,
            Experiment.LIVE_THEATRE_REFACTOR_GLOBAL,
            Experiment.AMAZON_IDENTITY_INTEGRATION,
            Experiment.FOLLOW_SUB_DURING_ADS_EXPERIMENT,
            Experiment.STREAM_DISPLAY_ADS,
            Experiment.AUDIO_ADS_BACKGROUND,
            Experiment.ADS_VIDEO_ASYNC_LOADING,
            Experiment.EXPANDABLE_ADS -> false

            Experiment.ADS_CLIENT_AD_ALLOCATOR -> true

            Experiment.CHAT_SETTINGS -> Flag.CHAT_SETTINGS.asBoolean()

            else -> org.isInOnGroupForBinaryExperimentOrg(p0).also { res ->
                LoggerImpl.devDebug { "${p0.experimentName} --> $p0, res --> $res" }
            }
        }
    }

    override fun isInRestrictedLocaleForExperiment(p0: Experiment): Boolean {
        return org.isInRestrictedLocaleForExperimentOrg(p0).also { res ->
            LoggerImpl.devDebug { "${p0.experimentName} --> $p0, res --> $res" }
        }
    }

    override fun getModelForExperimentId(p0: String): MiniExperimentModel {
        return org.getModelForExperimentIdOrg(p0)
    }

    override fun getUpdatedRemoteConfigurablesObservable(): Observable<MutableSet<RemoteConfigurable>> {
        return org.getUpdatedRemoteConfigurablesObservableOrg()
    }

    override fun refreshExperiments(p0: Int) {
        org.refreshExperimentsOrg(p0)
    }

    override fun refreshIfNeeded(p0: Int) {
        org.refreshIfNeededOrg(p0)
    }

    override fun updateEnabledGroupsForActiveExperiments(): MutableSet<RemoteConfigurable> {
        return org.updateEnabledGroupsForActiveExperimentsOrg()
    }

    companion object {
        private const val ANIMATED_EMOTES_ACTIVE = "active_11.5"
    }
}
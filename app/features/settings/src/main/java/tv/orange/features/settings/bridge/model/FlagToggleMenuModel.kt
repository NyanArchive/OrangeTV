package tv.orange.features.settings.bridge.model

import tv.orange.core.models.Flag
import tv.orange.core.models.Flag.Companion.valueBoolean

class FlagToggleMenuModel(flag: Flag) : ToggleMenuModel(
    primaryText = flag.titleRes ?: "",
    secondaryText = flag.summaryRes,
    auxiliaryText = null,
    state = flag.valueBoolean(),
    eventName = flag.prefKey
)
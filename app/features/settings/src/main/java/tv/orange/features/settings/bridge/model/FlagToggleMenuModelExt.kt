package tv.orange.features.settings.bridge.model

import tv.orange.core.ResourceManager
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean

class FlagToggleMenuModelExt(val flag: Flag) : ToggleMenuModelExt(
    primaryText = ResourceManager.get().getString(flag.titleRes!!),
    secondaryText = flag.summaryRes?.let { id ->
        ResourceManager.get().getString(id)
    },
    auxiliaryText = null,
    state = flag.asBoolean(),
    eventName = flag.prefKey
) {
    override fun getToggleState(): Boolean {
        return flag.asBoolean()
    }
}
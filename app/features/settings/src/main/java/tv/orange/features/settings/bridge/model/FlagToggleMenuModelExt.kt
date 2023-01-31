package tv.orange.features.settings.bridge.model

import tv.orange.core.ResourcesManagerCore
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean

class FlagToggleMenuModelExt(
    private val flag: Flag
) : ToggleMenuModelExt(
    primaryText = flag.titleResName?.let { resName ->
        ResourcesManagerCore.get().getString(resName = resName)
    } ?: "<NULL>",
    secondaryText = flag.summaryResName?.let { id ->
        ResourcesManagerCore.get().getString(id)
    },
    auxiliaryText = null,
    state = flag.asBoolean(),
    eventName = flag.preferenceKey
) {
    override fun getToggleState(): Boolean {
        return flag.asBoolean()
    }
}
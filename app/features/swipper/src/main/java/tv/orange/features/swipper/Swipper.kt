package tv.orange.features.swipper

import tv.orange.core.Core
import tv.orange.models.abc.Feature
import javax.inject.Inject

class Swipper @Inject constructor() : Feature {
    companion object {
        @JvmStatic
        fun get() = Core.getFeature(Swipper::class.java)
    }

    override fun onCreateFeature() {}
}
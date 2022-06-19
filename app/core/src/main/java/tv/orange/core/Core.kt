package tv.orange.core

import okhttp3.OkHttpClient
import tv.orange.core.di.CoreComponent
import tv.orange.core.di.DaggerCoreComponent
import javax.inject.Inject

class Core private constructor() {
    @Inject
    lateinit var client: OkHttpClient

    lateinit var coreComponent: CoreComponent

    fun initialize() {
        coreComponent = DaggerCoreComponent.factory().create()
        coreComponent.inject(this)
    }

    companion object {
        fun create(): Core {
            return Core()
        }
    }
}
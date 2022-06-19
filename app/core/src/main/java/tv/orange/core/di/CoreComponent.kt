package tv.orange.core.di

import dagger.Component
import tv.orange.core.Core
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class])
interface CoreComponent {
    fun inject(core: Core)

    @Component.Factory
    interface Factory {
        fun create(): CoreComponent
    }
}
package tv.orange.injector.di

import dagger.BindsInstance
import dagger.Component
import tv.twitch.android.app.consumer.dagger.DaggerAppComponent

@Component(modules = [OrangeComponentsModule::class])
@InjectorScope
interface InjectorComponent {
    val injector: tv.orange.injector.Injector

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance twitchComponent: DaggerAppComponent): InjectorComponent
    }
}
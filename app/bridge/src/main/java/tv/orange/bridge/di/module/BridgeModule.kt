package tv.orange.bridge.di.module

import dagger.Module
import dagger.Provides
import tv.orange.bridge.di.scope.BridgeScope
import tv.orange.core.Logger
import tv.orange.models.abc.TwitchComponentProvider
import tv.orange.models.abc.TCPProvider
import tv.twitch.android.app.core.ApplicationContext

@Module(includes = [BridgeComponentModule::class, BridgeFeatureModule::class])
class BridgeModule {
    @BridgeScope
    @Provides
    fun provideInjector(): TwitchComponentProvider {
        Logger.debug("called")
        val context = ApplicationContext.getInstance().getContext()
        if (context is TCPProvider) {
            return context.provideTCP()
        }

        throw IllegalStateException("context must provide injector")
    }
}
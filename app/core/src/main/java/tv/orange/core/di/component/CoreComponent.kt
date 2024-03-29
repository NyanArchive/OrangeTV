package tv.orange.core.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.orange.core.CoreHook
import tv.orange.core.PreferencesManagerCore
import tv.orange.core.di.module.CoreModule
import tv.orange.core.di.module.NetworkModule
import tv.orange.core.di.scope.AppScope
import tv.twitch.android.app.core.ThemeManager
import tv.twitch.android.core.user.TwitchAccountManager

@AppScope
@Component(modules = [CoreModule::class, NetworkModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    val context: Context

    val retrofitBuilder: Retrofit.Builder
    val defaultOkHttpClient: OkHttpClient

    val twitchAccountManager: TwitchAccountManager
    val themeManager: ThemeManager.Companion

    val preferencesManagerCore: PreferencesManagerCore

    val coreHook: CoreHook
}
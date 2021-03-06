package tv.orange.core.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.orange.core.Hook
import tv.orange.core.PreferenceManager
import tv.orange.core.di.module.CoreModule
import tv.orange.core.di.module.NetworkModule
import tv.orange.core.di.scope.AppScope

@AppScope
@Component(modules = [CoreModule::class, NetworkModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    val okHttpClient: OkHttpClient
    val retrofitBuilder: Retrofit.Builder

    val preferenceManager: PreferenceManager

    val coreHook: Hook
}
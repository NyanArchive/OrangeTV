package tv.orange.core.di

import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@AppScope
@Component(modules = [CoreModule::class])
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(): CoreComponent
    }

    val provideOkHttpClient: OkHttpClient
    val provideRetrofitBuilder: Retrofit.Builder
}
package tv.orange.core.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tv.orange.core.Logger
import tv.orange.core.di.scope.AppScope
import tv.orange.core.factory.StringConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient().newBuilder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "OrangeTV/0.1")
                        .build()
                )
            }
            .retryOnConnectionFailure(true).build()

        Logger.debug("Provide new instance: $client")
        return client
    }

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        val builder = Retrofit.Builder()
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(client)

        Logger.debug("Provide new instance: $builder")
        return builder
    }
}
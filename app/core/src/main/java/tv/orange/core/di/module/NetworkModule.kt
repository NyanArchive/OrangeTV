package tv.orange.core.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tv.orange.core.BuildConfigUtil
import tv.orange.core.di.scope.AppScope
import tv.orange.core.factory.StringConverterFactory
import tv.orange.core.models.flag.Flag
import tv.orange.core.models.flag.Flag.Companion.asBoolean
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    private val httpInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(DEFAULT_OKHTTP_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_OKHTTP_TIMEOUT, TimeUnit.MILLISECONDS).apply {
                addNetworkInterceptor { chain ->
                    chain.proceed(
                        chain.request().newBuilder().header(
                            "User-Agent",
                            BuildConfigUtil.userAgent
                        ).build()
                    )
                }
                if (Flag.OKHTTP_LOGGING.asBoolean()) {
                    addInterceptor(httpInterceptor)
                }
            }
            .retryOnConnectionFailure(true).build()
    }

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .client(client)
    }

    companion object {
        const val DEFAULT_OKHTTP_TIMEOUT = 5000L
    }
}
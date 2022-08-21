package tv.orange.core.di.module

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tv.orange.core.BuildConfigUtil
import tv.orange.core.BuildConfigUtil.USER_AGENT_TEMPLATE
import tv.orange.core.di.scope.AppScope
import tv.orange.core.factory.StringConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    private val buildNumber: Int by lazy {
        BuildConfigUtil.buildConfig.number
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .readTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header(
                            "User-Agent",
                            String.format(
                                Locale.ENGLISH,
                                USER_AGENT_TEMPLATE,
                                buildNumber
                            )
                        ).build()
                )
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
}
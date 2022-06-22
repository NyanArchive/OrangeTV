package tv.orange.features.emotes.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.emotes.component.data.api.BttvApi

@Module
class ApiModule {
    @EmotesScope
    @Provides
    fun provideBttvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.betterttv.net/").build()
    }

    @EmotesScope
    @Provides
    fun provideBttvApi(retrofit: Retrofit): BttvApi {
        return retrofit.create(BttvApi::class.java)
    }
}
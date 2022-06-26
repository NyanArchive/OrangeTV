package tv.orange.features.emotes.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.emotes.component.data.api.BttvApi
import tv.orange.features.emotes.component.data.api.StvApi
import tv.orange.features.emotes.di.scope.EmotesScope
import javax.inject.Named

@Module
class EmotesApiModule {
    @EmotesScope
    @Provides
    @Named("bttv")
    fun provideBttvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.betterttv.net/").build()
    }

    @EmotesScope
    @Provides
    @Named("stv")
    fun provideStvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.7tv.app/").build()
    }

    @EmotesScope
    @Provides
    fun provideBttvApi(@Named("bttv") retrofit: Retrofit): BttvApi {
        return retrofit.create(BttvApi::class.java)
    }

    @EmotesScope
    @Provides
    fun provideStvApi(@Named("stv") retrofit: Retrofit): StvApi {
        return retrofit.create(StvApi::class.java)
    }
}
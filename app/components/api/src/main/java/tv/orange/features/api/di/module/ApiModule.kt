package tv.orange.features.api.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.api.component.data.api.*
import tv.orange.features.api.di.scope.ApiScope
import javax.inject.Named

@Module
class ApiModule {
    @ApiScope
    @Provides
    @Named("bttv")
    fun provideBttvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.betterttv.net/").build()
    }

    @ApiScope
    @Provides
    @Named("stv")
    fun provideStvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.7tv.app/").build()
    }

    @ApiScope
    @Provides
    @Named("ffz")
    fun provideFfzRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.frankerfacez.com/").build()
    }

    @ApiScope
    @Provides
    @Named("chatterino")
    fun provideChatterinoRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.chatterino.com/").build()
    }

    @ApiScope
    @Provides
    @Named("itz")
    fun provideItzRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://itzalex.github.io/").build()
    }

    @ApiScope
    @Provides
    @Named("nop")
    fun provideNopRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.nopbreak.ru/").build()
    }

    @ApiScope
    @Provides
    @Named("ttsft")
    fun provideTTSFTRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.twitch.tyo.kwabang.net/").build()
    }

    @ApiScope
    @Provides
    @Named("twitching")
    fun provideTwitchingRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://workers.twitch-relay.wesub.io/").build()
    }

    @ApiScope
    @Provides
    fun provideBttvApi(@Named("bttv") retrofit: Retrofit): BttvApi {
        return retrofit.create(BttvApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideItzApi(@Named("itz") retrofit: Retrofit): ItzalexApi {
        return retrofit.create(ItzalexApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideStvApi(@Named("stv") retrofit: Retrofit): StvApi {
        return retrofit.create(StvApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideFfzApi(@Named("ffz") retrofit: Retrofit): FfzApi {
        return retrofit.create(FfzApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideChatterinoApi(@Named("chatterino") retrofit: Retrofit): ChatterinoApi {
        return retrofit.create(ChatterinoApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideNopApi(@Named("nop") retrofit: Retrofit): NopApi {
        return retrofit.create(NopApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideTTSFTApi(@Named("ttsft") retrofit: Retrofit): TTSFTApi {
        return retrofit.create(TTSFTApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideTwitchingApi(@Named("twitching") retrofit: Retrofit): TwitchingApi {
        return retrofit.create(TwitchingApi::class.java)
    }
}
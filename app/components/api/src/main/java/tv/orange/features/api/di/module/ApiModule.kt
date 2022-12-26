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
    @Named("lol")
    fun provideLolRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.ttv.lol/").build()
    }

    @ApiScope
    @Provides
    @Named("pas1")
    fun providePas1RetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://eu1.jupter.ga/").build()
    }

    @ApiScope
    @Provides
    @Named("pas2")
    fun providePas2RetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://eu2.jupter.ga/").build()
    }

    @ApiScope
    @Provides
    @Named("ffzap")
    fun provideFfzapRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.ffzap.com/").build()
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

    @ApiScope
    @Provides
    fun provideLolApi(@Named("lol") retrofit: Retrofit): LolApi {
        return retrofit.create(LolApi::class.java)
    }

    @ApiScope
    @Provides
    @Named("PAS1")
    fun providePas1Api(@Named("pas1") retrofit: Retrofit): PurpleAdblockApi {
        return retrofit.create(PurpleAdblockApi::class.java)
    }

    @ApiScope
    @Provides
    @Named("PAS2")
    fun providePas2Api(@Named("pas2") retrofit: Retrofit): PurpleAdblockApi {
        return retrofit.create(PurpleAdblockApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideFfzapApi(@Named("ffzap") retrofit: Retrofit): FFZAPApi {
        return retrofit.create(FFZAPApi::class.java)
    }
}
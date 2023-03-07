package tv.orange.features.api.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.api.component.data.api.*
import tv.orange.features.api.di.scope.*
import javax.inject.Named

@Module
class ApiModule {
    @ApiScope
    @Provides
    @Named(BTTV)
    fun provideBttvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.betterttv.net/").build()
    }

    @ApiScope
    @Provides
    @Named(STV_OLD_API)
    fun provideStvOldApiRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.7tv.app/").build()
    }

    @ApiScope
    @Provides
    @Named(STV)
    fun provideStvRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://7tv.io/").build()
    }

    @ApiScope
    @Provides
    @Named(FFZ)
    fun provideFfzRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.frankerfacez.com/").build()
    }

    @ApiScope
    @Provides
    @Named(CHATTERINO)
    fun provideChatterinoRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.chatterino.com/").build()
    }

    @ApiScope
    @Provides
    @Named(ITZ)
    fun provideItzRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://itzalex.github.io/").build()
    }

    @ApiScope
    @Provides
    @Named(NOP)
    fun provideNopRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.nopbreak.ru/").build()
    }

    @ApiScope
    @Provides
    @Named(TWITCHING)
    fun provideTwitchingRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://workers.twitch-relay.wesub.io/").build()
    }

    @ApiScope
    @Provides
    @Named(TTVLOL)
    fun provideLolRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.ttv.lol/").build()
    }

    @ApiScope
    @Provides
    @Named(PAS1)
    fun providePas1RetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://eu1.jupter.ga/").build()
    }

    @ApiScope
    @Provides
    @Named(PAS2)
    fun providePas2RetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://eu2.jupter.ga/").build()
    }

    @ApiScope
    @Provides
    @Named(FFZAP)
    fun provideFfzapRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.ffzap.com/").build()
    }

    @ApiScope
    @Provides
    fun provideBttvApi(@Named(BTTV) retrofit: Retrofit): BttvApi {
        return retrofit.create(BttvApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideItzApi(@Named(ITZ) retrofit: Retrofit): ItzalexApi {
        return retrofit.create(ItzalexApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideStvApi(@Named(STV) retrofit: Retrofit): StvApi {
        return retrofit.create(StvApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideStvOldApi(@Named(STV_OLD_API) retrofit: Retrofit): StvOldApi {
        return retrofit.create(StvOldApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideFfzApi(@Named(FFZ) retrofit: Retrofit): FfzApi {
        return retrofit.create(FfzApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideChatterinoApi(@Named(CHATTERINO) retrofit: Retrofit): ChatterinoApi {
        return retrofit.create(ChatterinoApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideNopApi(@Named(NOP) retrofit: Retrofit): NopApi {
        return retrofit.create(NopApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideTwitchingApi(@Named(TWITCHING) retrofit: Retrofit): TwitchingApi {
        return retrofit.create(TwitchingApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideLolApi(@Named(TTVLOL) retrofit: Retrofit): LolApi {
        return retrofit.create(LolApi::class.java)
    }

    @ApiScope
    @Provides
    @Named("PAS1")
    fun providePas1Api(@Named(PAS1) retrofit: Retrofit): PurpleAdblockApi {
        return retrofit.create(PurpleAdblockApi::class.java)
    }

    @ApiScope
    @Provides
    @Named("PAS2")
    fun providePas2Api(@Named(PAS2) retrofit: Retrofit): PurpleAdblockApi {
        return retrofit.create(PurpleAdblockApi::class.java)
    }

    @ApiScope
    @Provides
    fun provideFfzapApi(@Named(FFZAP) retrofit: Retrofit): FFZAPApi {
        return retrofit.create(FFZAPApi::class.java)
    }
}
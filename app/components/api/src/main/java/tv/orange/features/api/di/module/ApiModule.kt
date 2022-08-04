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
    @Named("nop")
    fun provideNopRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.nopbreak.ru/").build()
    }

    @ApiScope
    @Provides
    fun provideBttvApi(@Named("bttv") retrofit: Retrofit): BttvApi {
        return retrofit.create(BttvApi::class.java)
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
}
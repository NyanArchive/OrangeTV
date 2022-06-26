package tv.orange.features.badges.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.badges.component.data.api.FfzApi
import tv.orange.features.badges.di.scope.BadgesScope

@Module
class BadgesApiModule {
    @BadgesScope
    @Provides
    fun provideFfzRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://api.frankerfacez.com/").build()
    }

    @BadgesScope
    @Provides
    fun provideFfzApi(retrofit: Retrofit): FfzApi {
        return retrofit.create(FfzApi::class.java)
    }
}
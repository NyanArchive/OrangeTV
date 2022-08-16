package tv.orange.features.pronouns.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.core.di.module.NetworkModule
import tv.orange.features.pronouns.component.data.api.PronounsApi
import tv.orange.features.pronouns.di.scope.PronounScope
import javax.inject.Named

@Module
class PronounModule {
    @PronounScope
    @Provides
    @Named("test")
    fun providePronounsRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://pronouns.alejo.io/").build()
    }

    @PronounScope
    @Provides
    fun providePronounsApi(@Named("test") retrofit: Retrofit): PronounsApi {
        return retrofit.create(PronounsApi::class.java)
    }
}
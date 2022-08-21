package tv.orange.features.pronouns.di.module

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import tv.orange.features.pronouns.component.data.api.PronounsApi
import tv.orange.features.pronouns.di.scope.PronounScope

@Module
class PronounModule {
    @PronounScope
    @Provides
    fun providePronounsRetrofitClient(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl("https://pronouns.alejo.io/").build()
    }

    @PronounScope
    @Provides
    fun providePronounsApi(retrofit: Retrofit): PronounsApi {
        return retrofit.create(PronounsApi::class.java)
    }
}
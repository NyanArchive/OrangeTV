package tv.orange.features.core.di

import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tv.orange.core.di.CoreComponent
import tv.orange.features.core.CoreFeature

@CoreFeatureScope
@Component(dependencies = [CoreComponent::class], modules = [CoreFeatureModule::class])
interface CoreFeatureComponent {
    val provideOkHttpClient: OkHttpClient
    val provideRetrofitBuilder: Retrofit.Builder
}
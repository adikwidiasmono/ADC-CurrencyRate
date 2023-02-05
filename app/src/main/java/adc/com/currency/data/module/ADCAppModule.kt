package adc.com.currency.data.module

import adc.com.currency.data.ADCRepository
import adc.com.currency.data.api.ADCApi
import adc.com.currency.data.api.ADCRemoteSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ADCAppModule {

    @Provides
    @Singleton
    fun provideRemoteApi(retrofit: Retrofit): ADCApi {
        return retrofit.create(ADCApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteSource(api: ADCApi): ADCRemoteSource {
        return ADCRemoteSource(api)
    }

    @Provides
    @Singleton
    fun provideRepository(
        remoteSource: ADCRemoteSource
    ): ADCRepository {
        return ADCRepository(remoteSource)
    }
}
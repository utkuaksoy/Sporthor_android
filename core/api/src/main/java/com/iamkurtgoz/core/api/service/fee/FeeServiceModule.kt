package com.iamkurtgoz.core.api.service.fee

import com.iamkurtgoz.core.common.qualifiers.QualifierRetrofitWithAuthorizationAndAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeeServiceModule {

    @Provides
    @Singleton
    fun providesFeeService(@QualifierRetrofitWithAuthorizationAndAuthenticator retrofit: Retrofit): FeeService {
        return retrofit.create(FeeService::class.java)
    }
}

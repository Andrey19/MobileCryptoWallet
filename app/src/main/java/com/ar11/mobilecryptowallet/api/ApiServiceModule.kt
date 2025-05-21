package com.ar11.mobilecryptowallet.api


import com.ar11.mobilecryptowallet.auth.AppAuth2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {
    @Provides
    @Singleton
    fun provideApiService(auth2: AppAuth2): ApiService {
        return retrofit(okhttp(loggingInterceptor(), authInterceptor(auth2)))
            .create(ApiService::class.java)
    }

}

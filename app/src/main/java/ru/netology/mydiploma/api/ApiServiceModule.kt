package ru.netology.mydiploma.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.mydiploma.api.connection.okHttpClient
import ru.netology.mydiploma.api.connection.retrofitClient
import ru.netology.mydiploma.api.interceptors.authInterceptor
import ru.netology.mydiploma.api.interceptors.loggingInterceptor
import ru.netology.mydiploma.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    @Singleton
    @Provides
    fun provideAuthApiService(): AuthApiService {
        return retrofitClient(okHttpClient(loggingInterceptor()))
            .create(AuthApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideEventApiService(auth: AppAuth): EventApiService {
        return retrofitClient(okHttpClient(loggingInterceptor(), authInterceptor(auth)))
            .create(EventApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideJobApiService(auth: AppAuth): JobApiService {
        return retrofitClient(okHttpClient(loggingInterceptor(), authInterceptor(auth)))
            .create(JobApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePostApiService(auth: AppAuth): PostApiService {
        return retrofitClient(okHttpClient(loggingInterceptor(), authInterceptor(auth)))
            .create(PostApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApiService(): UserApiService {
        return retrofitClient(okHttpClient(loggingInterceptor()))
            .create(UserApiService::class.java)
    }
}
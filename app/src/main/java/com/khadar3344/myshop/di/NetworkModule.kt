package com.khadar3344.myshop.di

import android.content.Context
import com.khadar3344.myshop.util.AppConfig
import com.khadar3344.myshop.data.network.api.ApiService
import com.khadar3344.myshop.data.network.repository.MockNetworkRepositoryImpl
import com.khadar3344.myshop.data.network.repository.NetworkRepository
import com.khadar3344.myshop.data.network.repository.NetworkRepositoryImpl
import com.khadar3344.myshop.notifications.NotificationHelper
import com.khadar3344.myshop.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        apiService: ApiService
    ): NetworkRepository {
        // Use mock repository for testing
        return if (AppConfig.USE_MOCK_DATA) {
            MockNetworkRepositoryImpl()
        } else {
            NetworkRepositoryImpl(apiService)
        }
    }

    @Provides
    @Singleton
    fun provideNotificationHelper(@ApplicationContext context: Context): NotificationHelper {
        return NotificationHelper(context)
    }
}

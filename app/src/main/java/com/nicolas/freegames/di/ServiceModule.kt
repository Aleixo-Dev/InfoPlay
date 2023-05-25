package com.nicolas.freegames.di

import com.nicolas.freegames.data.repository.FreeGameDataSource
import com.nicolas.freegames.data.repository.FreeGameDataSourceImpl
import com.nicolas.freegames.data.repository.FreeGameRepository
import com.nicolas.freegames.data.repository.FreeGameRepositoryImpl
import com.nicolas.freegames.data.service.FreeGameService
import com.nicolas.freegames.utils.Consts
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)

    @Provides
    fun provideRetrofitService(): FreeGameService {
        return Retrofit.Builder()
            .baseUrl(Consts.BASE_URL)
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FreeGameService::class.java)
    }

    @Provides
    fun provideDataSource(
        service: FreeGameService
    ) : FreeGameDataSource {
        return FreeGameDataSourceImpl(service)
    }

    @Provides
    fun provideRepository(
        dataSource: FreeGameDataSource
    ) : FreeGameRepository {
        return FreeGameRepositoryImpl(dataSource)
    }
}
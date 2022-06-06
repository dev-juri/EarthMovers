package com.earthmovers.www.di

import com.earthmovers.www.BuildConfig
import com.earthmovers.www.data.remote.EarthMoversService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient.Builder): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client.build())
            .baseUrl(BuildConfig.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient.Builder {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor.apply {
                if (BuildConfig.DEBUG) {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                } else {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
                }
            })
    }

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit.Builder): EarthMoversService {
        return retrofit.build()
            .create(EarthMoversService::class.java)
    }

}
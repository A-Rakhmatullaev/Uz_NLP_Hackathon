package com.example.tts_test.di

import com.example.tts_test.data.remote.Api
import com.example.tts_test.data.remote.AuthInterceptor
import com.example.tts_test.data.remote.NetworkInterceptor
import com.example.tts_test.data.repositories.HackathonInterfaceImpl
import com.example.tts_test.domain.repositories.HackathonInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //    @Provides
    //    @Singleton
    //    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    //        level = HttpLoggingInterceptor.Level.BODY
    //    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkInterceptor: NetworkInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(authInterceptor)
        .addNetworkInterceptor(networkInterceptor)
        .callTimeout(40, TimeUnit.SECONDS)
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): Api = Retrofit
        .Builder()
        .baseUrl("http://192.168.1.118:4000/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)

    @Provides
    @Singleton
    fun provideHackathonInterface(api: Api): HackathonInterface = HackathonInterfaceImpl(api)
}
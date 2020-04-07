package com.example.catfact.di

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.remote.RemoteCatFactsApi
import com.example.catfact.data.remote.RemoteCatFactsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class RemoteRepository

@Module
class RetrofitModule {

    private val baseUrl = "https://cat-fact.herokuapp.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideCatsSource(retrofit: Retrofit): RemoteCatFactsApi {
        return retrofit.create(RemoteCatFactsApi::class.java)
    }


    @Provides
    @RemoteRepository
    @Singleton
    fun provideRemoteCatFactsRepository(remoteCatFactsApi: RemoteCatFactsApi): CatFactsRepository {
        return RemoteCatFactsRepository(remoteCatFactsApi)
    }
}
package com.example.catfact.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.sources.remote.RemoteApi
import com.example.catfact.sources.remote.RemoteCatFactsRepository
import com.example.catfact.util.NetworkManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(API_DATE_FORMAT)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context) : ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    @Singleton
    fun provideConnectionManager(connectivityManager: ConnectivityManager) : NetworkManager {
        return NetworkManager(connectivityManager)
    }

    @Provides
    @Singleton
    fun provideCatsSource(retrofit: Retrofit): RemoteApi {
        return retrofit.create(RemoteApi::class.java)
    }

    @Provides
    @RemoteRepository
    @Singleton
    fun provideRemoteCatFactsRepository(
        remoteApi: RemoteApi,
        networkManager: NetworkManager
    ): CatFactsRepository {

        return RemoteCatFactsRepository(remoteApi, networkManager)
    }

    companion object {
        private val API_DATE_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
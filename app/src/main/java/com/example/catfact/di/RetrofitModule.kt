package com.example.catfact.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.sources.remote.RemoteApi
import com.example.catfact.sources.remote.RemoteCatFactsRepository
import com.example.catfact.util.network.NetworkManager
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
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .setDateFormat(API_DATE_FORMAT)
            .create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @Provides
    @Singleton
    fun provideConnectionManager(connectivityManager: ConnectivityManager): NetworkManager =
        NetworkManager(connectivityManager)

    @Provides
    @Singleton
    fun provideCatsSource(retrofit: Retrofit): RemoteApi =
        retrofit.create(RemoteApi::class.java)

    @Provides
    @RemoteRepository
    @Singleton
    fun provideRemoteCatFactsRepository(remoteApi: RemoteApi): CatFactsRepository =
        RemoteCatFactsRepository(remoteApi)

    companion object {
        private const val API_DATE_FORMAT: String = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
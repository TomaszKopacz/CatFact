package com.example.catfact.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.catfact.util.NetworkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

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
}
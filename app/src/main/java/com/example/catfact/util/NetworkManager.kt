package com.example.catfact.util

import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkManager @Inject constructor(
    private val connectivityManager: ConnectivityManager
) {

    fun isConnected() : Boolean {
        val networkInfo = connectivityManager.activeNetworkInfo
        return  networkInfo != null && networkInfo.isConnected
    }
}
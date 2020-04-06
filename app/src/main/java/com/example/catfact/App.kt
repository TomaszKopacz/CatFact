package com.example.catfact

import android.app.Application
import com.example.catfact.di.AppComponent
import com.example.catfact.di.DaggerAppComponent

class App : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
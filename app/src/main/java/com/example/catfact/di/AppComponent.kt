package com.example.catfact.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SubcomponentsModule::class,
        RetrofitModule::class,
        DatabaseModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun catsComponent(): CatFactsComponent.Factory
}
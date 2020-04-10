package com.example.catfact.di

import android.content.Context
import androidx.room.Room
import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.sources.local.CatFactsDatabase
import com.example.catfact.sources.local.CatFactsLocalApi
import com.example.catfact.sources.local.LocalCatFactsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    private val db = "CatFact"

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : CatFactsDatabase {
        return Room.databaseBuilder(context, CatFactsDatabase::class.java, db).build()
    }

    @Provides
    @Singleton
    fun provideCatFactsSource(db: CatFactsDatabase) : CatFactsLocalApi {
        return db.dao()
    }

    @Provides
    @LocalRepository
    @Singleton
    fun provideLocalCatFactsRepository(catFactsLocalApi: CatFactsLocalApi) : CatFactsRepository {
        return LocalCatFactsRepository(catFactsLocalApi)
    }
}
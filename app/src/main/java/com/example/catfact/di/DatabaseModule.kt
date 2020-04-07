package com.example.catfact.di

import android.content.Context
import androidx.room.Room
import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.local.CatFactsDatabase
import com.example.catfact.data.local.LocalCatFactsRepository
import com.example.catfact.data.local.LocalCatFactsApi
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class LocalRepository

@Module
class DatabaseModule {

    private val db = "CatFact"

    @Provides
    @Singleton
    fun provideDatabase(context: Context) : CatFactsDatabase {
        return Room.databaseBuilder(context, CatFactsDatabase::class.java, db).build()
    }

    @Provides
    @Singleton
    fun provideCatFactsSource(db: CatFactsDatabase) : LocalCatFactsApi {
        return db.dao()
    }

    @Provides
    @LocalRepository
    @Singleton
    fun provideLocalCatFactsRepository(localCatFactsApi: LocalCatFactsApi) : CatFactsRepository {
        return LocalCatFactsRepository(localCatFactsApi)
    }
}
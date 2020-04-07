package com.example.catfact.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catfact.model.CatFact
import javax.inject.Singleton

@Singleton
@Database(entities = [CatFact::class], version = 1)
abstract class CatFactsDatabase : RoomDatabase() {

    abstract fun dao() : LocalCatFactsApi
}
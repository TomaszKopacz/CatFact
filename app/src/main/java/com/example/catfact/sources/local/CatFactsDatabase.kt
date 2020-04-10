package com.example.catfact.sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catfact.model.CatFact
import com.example.catfact.util.date.DateConverters
import javax.inject.Singleton

@Singleton
@Database(entities = [CatFact::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class CatFactsDatabase : RoomDatabase() {

    abstract fun dao() : CatFactsLocalApi
}
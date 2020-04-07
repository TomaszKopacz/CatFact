package com.example.catfact.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catfact.model.CatFact
import javax.inject.Singleton

@Singleton
@Dao
interface LocalCatFactsApi {

    @Query("SELECT * FROM cat_facts")
    fun getAll(): List<CatFact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAll(catFacts: List<CatFact>)
}
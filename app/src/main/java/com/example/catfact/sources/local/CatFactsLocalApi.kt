package com.example.catfact.sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catfact.model.CatFact
import javax.inject.Singleton

@Singleton
@Dao
interface CatFactsLocalApi {

    @Query("SELECT * FROM cat_facts")
    fun getAll(): List<CatFact>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createAll(catFacts: List<CatFact>)

    @Query("DELETE FROM cat_facts")
    fun deleteAll()
}
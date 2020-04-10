package com.example.catfact.sources

import com.example.catfact.model.CatFact
import com.example.catfact.model.Result

interface CatFactsRepository {

    suspend fun getAll() : Result<List<CatFact>>
    suspend fun getSome(number: Int) : Result<List<CatFact>>
    suspend fun updateAll(catFacts: List<CatFact>)
}
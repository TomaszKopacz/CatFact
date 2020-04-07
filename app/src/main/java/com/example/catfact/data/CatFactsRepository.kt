package com.example.catfact.data

import com.example.catfact.model.CatFact

interface CatFactsRepository {

    suspend fun getAll() : Result<List<CatFact>>
    suspend fun createAll(catFacts: List<CatFact>)
}
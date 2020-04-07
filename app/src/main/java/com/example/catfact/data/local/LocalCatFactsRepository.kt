package com.example.catfact.data.local

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Result
import com.example.catfact.model.CatFact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCatFactsRepository @Inject constructor(
    private val localCatFactsApi: LocalCatFactsApi
) : CatFactsRepository{

    override suspend fun getAll(): Result<List<CatFact>> {
        val response = localCatFactsApi.getAll()

        return if (response.isNotEmpty()) {
            Result.Success(response)

        } else {
            Result.Failure("Cannot get results")
        }
    }

    override suspend fun createAll(catFacts: List<CatFact>) {
        localCatFactsApi.createAll(catFacts)
    }
}
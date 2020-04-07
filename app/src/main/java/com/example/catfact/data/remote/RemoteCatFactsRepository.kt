package com.example.catfact.data.remote

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Result
import com.example.catfact.model.CatFact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCatFactsRepository @Inject constructor(
    private val remoteCatFactsApi: RemoteCatFactsApi
) : CatFactsRepository {

    override suspend fun getAll(): Result<List<CatFact>> {
        val response = remoteCatFactsApi.getCatFacts()

        return if (response.isSuccessful && response.body() != null) {
            Result.Success(response.body()!!.facts)

        } else {
            Result.Failure("Cannot download the facts")
        }
    }

    override suspend fun createAll(catFacts: List<CatFact>) {
        // Do nothing in this case
    }
}
package com.example.catfact.data.local

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Message
import com.example.catfact.data.Result
import com.example.catfact.model.CatFact
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCatFactsRepository @Inject constructor(
    private val localCatFactsApi: LocalCatFactsApi
) : CatFactsRepository {

    override suspend fun getAll(): Result<List<CatFact>> {
        return try {
            val response = localCatFactsApi.getAll()

            when (response.isNotEmpty()) {
                true -> Result.Success(response)
                false -> Result.Failure(Message(Message.LOCAL_DATABASE_EMPTY))
            }

        } catch (e: Exception) {
            Result.Failure(Message(Message.LOCAL_DATABASE_QUERY_FAILED))
        }
    }

    override suspend fun createAll(catFacts: List<CatFact>) {
        try {
            localCatFactsApi.createAll(catFacts)

        } catch (e: Exception) {
            Result.Failure(Message(Message.LOCAL_DATABASE_QUERY_FAILED))
        }
    }
}
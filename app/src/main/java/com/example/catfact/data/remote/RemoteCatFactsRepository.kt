package com.example.catfact.data.remote

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Message
import com.example.catfact.data.Result
import com.example.catfact.model.CatFact
import com.example.catfact.model.CatFactsResponse
import com.example.catfact.util.NetworkManager
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCatFactsRepository @Inject constructor(
    private val remoteCatFactsApi: RemoteCatFactsApi,
    private val networkManager: NetworkManager
) : CatFactsRepository {

    override suspend fun getAll(): Result<List<CatFact>> {

        return when (networkManager.isConnected()) {
            true -> doGetAll()
            false -> Result.Failure(Message(Message.NO_INTERNET_CONNECTION))
        }
    }

    private suspend fun doGetAll() : Result<List<CatFact>> {
        val response = remoteCatFactsApi.getCatFacts()

        return if (response.isSuccessful) {
            checkIsResultEmpty(response)

        } else {
            Result.Failure(Message(Message.REMOTE_DATABASE_QUERY_FAILED))
        }
    }

    private fun checkIsResultEmpty(response: Response<CatFactsResponse>): Result<List<CatFact>> {
        return if (response.body() != null || response.body()!!.facts.isEmpty())
            Result.Success(response.body()!!.facts)

        else
            Result.Failure(Message(Message.REMOTE_DATABASE_EMPTY))
    }

    override suspend fun createAll(catFacts: List<CatFact>) {
        // Do nothing in this case
    }
}
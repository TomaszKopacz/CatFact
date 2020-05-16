package com.example.catfact.sources.remote

import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import com.example.catfact.model.CatFact
import com.example.catfact.util.network.NetworkManager
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCatFactsRepository (
    private val remoteApi: RemoteApi,
    private val networkManager: NetworkManager
) : CatFactsRepository {

    override suspend fun getSome(number: Int): Result<List<CatFact>> {
        return when (networkManager.isConnected()) {
            true -> doGetFacts(number)
            false -> Result.Failure(
                Message(
                    Message.NO_INTERNET_CONNECTION
                )
            )
        }
    }

    private suspend fun doGetFacts(number: Int) : Result<List<CatFact>> {
        val response = remoteApi.getSomeFacts(CAT, number)

        return if (response.isSuccessful) {
            checkIsResultEmpty(response)

        } else {
            Result.Failure(Message(Message.REMOTE_DATABASE_QUERY_FAILED))
        }
    }

    private fun checkIsResultEmpty(response: Response<List<CatFact>>): Result<List<CatFact>> {
        return if (response.body() != null || response.body()!!.isEmpty())
            Result.Success(response.body()!!)

        else
            Result.Failure(Message(Message.REMOTE_DATABASE_EMPTY))
    }

    override suspend fun getAll(): Result<List<CatFact>> {
        // Do nothing in this case

        return Result.Failure(
            Message(
                Message.REMOTE_DATABASE_QUERY_FAILED
            )
        )
    }


    override suspend fun updateAll(catFacts: List<CatFact>) {
        // Do nothing in this case
    }

    override fun getCat(): Observable<CatFact> {
        return remoteApi.getOneFact()
    }

    companion object {
        private const val CAT: String = "cat"
    }
}
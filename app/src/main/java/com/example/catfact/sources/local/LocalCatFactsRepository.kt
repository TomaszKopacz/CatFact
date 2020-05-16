package com.example.catfact.sources.local

import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.model.Message
import com.example.catfact.model.Result
import com.example.catfact.model.CatFact
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCatFactsRepository (
    private val catFactsLocalApi: CatFactsLocalApi
) : CatFactsRepository {

    override suspend fun getAll(): Result<List<CatFact>> {
        return try {
            val response = catFactsLocalApi.getAll()

            when (response.isNotEmpty()) {
                true -> Result.Success(response)
                false -> Result.Failure(
                    Message(
                        Message.LOCAL_DATABASE_EMPTY
                    )
                )
            }

        } catch (e: Exception) {
            Result.Failure(Message(Message.LOCAL_DATABASE_QUERY_FAILED))
        }
    }

    override suspend fun getSome(number: Int): Result<List<CatFact>> {
        // Do nothing in this case

        return Result.Failure(
            Message(
                Message.LOCAL_DATABASE_QUERY_FAILED
            )
        )
    }

    override suspend fun updateAll(catFacts: List<CatFact>) {
        catFactsLocalApi.deleteAll()
        catFactsLocalApi.createAll(catFacts)
    }

    override fun getCat(): Observable<CatFact> {
        return Observable.create(null)
    }
}
package com.example.catfact.cats

import com.example.catfact.data.CatsRepository
import com.example.catfact.model.CatFact
import com.example.catfact.model.CatFactsResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsFacade @Inject constructor(private val catsRepo: CatsRepository) {

    suspend fun getCatFacts() : Response<CatFactsResponse> {
        return catsRepo.getCatsFacts()
    }
}
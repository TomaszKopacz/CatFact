package com.example.catfact.data.remote

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.model.CatFactsResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FactsRemoteRepository @Inject constructor(
    private val factsRemoteApi: FactsRemoteApi
) : CatFactsRepository {

    override suspend fun getCatFacts(): Response<CatFactsResponse> = factsRemoteApi.getCatFacts()
}
package com.example.catfact.data

import com.example.catfact.model.CatFactsResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatsRepository @Inject constructor(private val catsApi: CatsApi) {

    suspend fun getCatsFacts(): Response<CatFactsResponse> = catsApi.getCatsFacts()
}
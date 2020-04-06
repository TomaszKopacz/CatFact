package com.example.catfact.data

import com.example.catfact.model.CatFact
import com.example.catfact.model.CatFactsResponse
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface CatsApi {

    @GET("facts")
    suspend fun getCatsFacts(): Response<CatFactsResponse>
}
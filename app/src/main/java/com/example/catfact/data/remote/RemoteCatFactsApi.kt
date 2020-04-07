package com.example.catfact.data.remote

import com.example.catfact.model.CatFactsResponse
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface RemoteCatFactsApi {

    @GET("facts")
    suspend fun getCatFacts(): Response<CatFactsResponse>
}
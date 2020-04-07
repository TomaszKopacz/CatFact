package com.example.catfact.data

import com.example.catfact.model.CatFactsResponse
import retrofit2.Response

interface CatFactsApi {
    suspend fun getCatFacts() : Response<CatFactsResponse>
}
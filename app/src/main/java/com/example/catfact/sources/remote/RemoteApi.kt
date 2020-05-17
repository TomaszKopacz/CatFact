package com.example.catfact.sources.remote

import com.example.catfact.model.CatFact
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface RemoteApi {

    @GET("facts/random")
    suspend fun getSomeFacts(
        @Query("animal_type") animalType: String,
        @Query("amount") number: Int
    ): Response<List<CatFact>>

    @GET("facts/random?animal_type=cat&amount=1")
    fun getOneFact(): Call<CatFact>
}
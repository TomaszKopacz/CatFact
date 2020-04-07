package com.example.catfact.cats

import android.util.Log
import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Result
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository
) {

    suspend fun getCatFacts(): Result<List<CatFact>> {
        return fetchCatFacts()
    }

    private suspend fun fetchCatFacts(): Result<List<CatFact>> {
        val remoteResponse = remoteRepo.getCatFacts()

        if (remoteResponse.isSuccessful && remoteResponse.body() != null) {
            Log.d("CatFact", "Success")
        }

        return Result.Success(remoteResponse.body()!!.facts)
    }

//    private suspend fun fetchCatFacts(): Result<List<CatFact>> {
//        val remoteResponse = getRemoteResponse()
//
//        if (remoteResponse is Result.Success) {
//            updateLocalSource()
//        }
//
//        return getLocalResponse()
//    }
//
//    private suspend fun getRemoteResponse(): Result<List<CatFact>> {
//
//    }
//
//    private suspend fun getLocalResponse(): Result<List<CatFact>> {
//
//    }
//
//    private suspend fun updateLocalSource() {
//
//    }
}
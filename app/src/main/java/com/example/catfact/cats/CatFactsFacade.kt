package com.example.catfact.cats

import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Result
import com.example.catfact.di.LocalRepository
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import com.example.catfact.util.NetworkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository,
    @LocalRepository private val localRepo: CatFactsRepository
) {

    @Inject
    lateinit var networkManager: NetworkManager

    suspend fun getCatFacts(): Result<List<CatFact>> {
        return fetchCatFacts()
    }

    private suspend fun fetchCatFacts(): Result<List<CatFact>> {
        synchronizeRemoteAndLocalSources()

        return getLocalResponse()
    }

    private suspend fun synchronizeRemoteAndLocalSources() {
        if (networkManager.isConnected()) {
            val remoteResponse = getRemoteResponse()

            if (remoteResponse is Result.Success) {
                updateLocalDatabase(remoteResponse.data)
            }
        }
    }

    private suspend fun getRemoteResponse(): Result<List<CatFact>> = remoteRepo.getAll()

    private suspend fun getLocalResponse(): Result<List<CatFact>> = localRepo.getAll()

    private suspend fun updateLocalDatabase(catsFacts: List<CatFact>) = localRepo.createAll(catsFacts)
}
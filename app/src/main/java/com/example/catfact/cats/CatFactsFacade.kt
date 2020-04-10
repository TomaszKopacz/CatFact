package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catfact.sources.CatFactsRepository
import com.example.catfact.model.Result
import com.example.catfact.di.ActivityScope
import com.example.catfact.di.LocalRepository
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

@ActivityScope
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository,
    @LocalRepository private val localRepo: CatFactsRepository
) {

    private val catFacts = MutableLiveData<Result<List<CatFact>>>()

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFacts

    suspend fun getCatFacts() {
        if (sendLoadingEnabled)
            emitLoading()

        synchronizeRemoteAndLocalSources()
        fetchLocalSourceResult()
    }

    private suspend fun synchronizeRemoteAndLocalSources() {
        when (val remoteResult = getRemoteCatFacts(NUM_OF_ELEMENTS)) {
            is Result.Success -> updateLocalDatabase(remoteResult.data)
            is Result.Failure -> emitWarning(
                Result.Warning(remoteResult.message))
        }
    }

    private suspend fun fetchLocalSourceResult() {
        when (val localResult = getLocalCatFacts()) {
            is Result.Success -> emitCatFacts(localResult)
            is Result.Failure -> emitError(localResult)
        }
    }

    private suspend fun getRemoteCatFacts(number: Int): Result<List<CatFact>> =
        remoteRepo.getSome(number)

    private suspend fun getLocalCatFacts(): Result<List<CatFact>> =
        localRepo.getAll()

    private suspend fun updateLocalDatabase(catsFacts: List<CatFact>) =
        localRepo.updateAll(catsFacts)

    private fun emitLoading() {
        catFacts.postValue(Result.Loading)
    }

    private fun emitError(result: Result.Failure) {
        catFacts.postValue(result)
    }

    private fun emitWarning(result: Result.Warning) {
        catFacts.postValue(result)
    }

    private fun emitCatFacts(result: Result.Success<List<CatFact>>) {
        catFacts.postValue(Result.Success(result.data))
    }

    private var sendLoadingEnabled: Boolean = true

    @TestOnly
    fun disableLoadingStatusSending() {
        sendLoadingEnabled = false
    }

    companion object {
        private const val NUM_OF_ELEMENTS: Int = 30
    }
}
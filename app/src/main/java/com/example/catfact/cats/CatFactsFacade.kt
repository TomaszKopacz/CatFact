package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catfact.data.CatFactsRepository
import com.example.catfact.data.Result
import com.example.catfact.di.ActivityScope
import com.example.catfact.di.LocalRepository
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import javax.inject.Inject

@ActivityScope
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository,
    @LocalRepository private val localRepo: CatFactsRepository
) {

    private val catFacts = MutableLiveData<Result<List<CatFact>>>()

    fun catFactsObservable() : LiveData<Result<List<CatFact>>> = catFacts

    suspend fun getCatFacts() {
        emitLoading()

        synchronizeRemoteAndLocalSources()
        fetchLocalSourceResult()
    }

    private suspend fun synchronizeRemoteAndLocalSources() {
        when (val remoteResult = getRemoteCatFacts()) {
            is  Result.Success -> updateLocalDatabase(remoteResult.data)
            is Result.Failure -> emitWarning(Result.Warning(remoteResult.message))
        }
    }

    private suspend fun fetchLocalSourceResult() {
        when (val localResult = getLocalCatFacts()) {
            is Result.Success -> emitCatFacts(localResult)
            is Result.Failure -> emitError(localResult)
        }
    }

    private suspend fun getRemoteCatFacts(): Result<List<CatFact>> = remoteRepo.getAll()

    private suspend fun getLocalCatFacts(): Result<List<CatFact>> = localRepo.getAll()

    private suspend fun updateLocalDatabase(catsFacts: List<CatFact>) =
        localRepo.createAll(catsFacts)

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
        val newCatFacts = graspRandomElements(result.data, LIST_SIZE)
        catFacts.postValue(Result.Success(newCatFacts))
    }

    private fun graspRandomElements(list: List<CatFact>, number: Int) : List<CatFact> {
        return list.shuffled().subList(0, number)
    }

    companion object {
        private const val LIST_SIZE: Int = 30
    }
}
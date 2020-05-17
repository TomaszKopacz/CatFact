package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catfact.di.ActivityScope
import com.example.catfact.di.LocalRepository
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import com.example.catfact.sources.CatFactsRepository
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository,
    @LocalRepository private val localRepo: CatFactsRepository
) {

    private val catFacts = MutableLiveData<Result<List<CatFact>>>()

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFacts

    fun getOneFact(): Observable<Result<CatFact>> {
        return remoteRepo.getCat()
    }

    suspend fun getCatFacts(number: Int) {
        synchronizeRemoteAndLocalSources(number)
        fetchLocalSourceResult()
    }

    private suspend fun synchronizeRemoteAndLocalSources(number: Int) {
        withContext(Dispatchers.IO) {
            when (val remoteResult = getRemoteCatFacts(number)) {
                is Result.Success -> updateLocalDatabase(remoteResult.data)
                is Result.Failure -> emitResult(remoteResult)
            }
        }
    }

    private suspend fun fetchLocalSourceResult() {
        withContext(Dispatchers.IO) {
            emitResult(getLocalCatFacts())
        }
    }

    private suspend fun getRemoteCatFacts(number: Int): Result<List<CatFact>> =
        remoteRepo.getSome(number)

    private suspend fun getLocalCatFacts(): Result<List<CatFact>> =
        localRepo.getAll()

    private suspend fun updateLocalDatabase(catsFacts: List<CatFact>) =
        localRepo.updateAll(catsFacts)

    private fun emitResult(result: Result<List<CatFact>>) = catFacts.postValue(result)
}
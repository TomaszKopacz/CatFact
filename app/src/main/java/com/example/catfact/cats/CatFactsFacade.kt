package com.example.catfact.cats

import com.example.catfact.di.ActivityScope
import com.example.catfact.di.RemoteRepository
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import com.example.catfact.sources.CatFactsRepository
import io.reactivex.Observable
import javax.inject.Inject

@ActivityScope
class CatFactsFacade @Inject constructor(
    @RemoteRepository private val remoteRepo: CatFactsRepository
) {

    fun downloadRandomCatFacts(number: Int): Observable<Result<List<CatFact>>> =
        remoteRepo.getCatFacts(number)
}
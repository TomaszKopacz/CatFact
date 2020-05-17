package com.example.catfact.sources

import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import io.reactivex.Observable

interface CatFactsRepository {

    fun getCatFacts(number: Int) : Observable<Result<List<CatFact>>>
}
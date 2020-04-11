package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catfact.di.ActivityScope
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScope
class CatsViewModel @Inject constructor(private val catFactsFacade: CatFactsFacade): ViewModel() {

    private val chosenCatFact = MutableLiveData<CatFact>()

    suspend fun downloadCatFacts(number: Int) = catFactsFacade.getCatFacts(number)

    fun onCatFactChosen(catFact: CatFact) = emitCatFact(catFact)

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFactsFacade.catFactsObservable()

    fun chosenCatFactObservable(): LiveData<CatFact> = chosenCatFact

    private fun emitCatFact(catFact: CatFact) = chosenCatFact.postValue(catFact)
}
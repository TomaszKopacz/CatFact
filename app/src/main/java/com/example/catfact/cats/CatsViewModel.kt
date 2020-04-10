package com.example.catfact.cats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catfact.di.ActivityScope
import com.example.catfact.model.CatFact
import com.example.catfact.model.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityScope
class CatsViewModel @Inject constructor(private val catFactsFacade: CatFactsFacade): ViewModel() {

    private val chosenCatFact = MutableLiveData<CatFact>()

    init {
        downloadCatFacts()
    }

    fun downloadCatFacts() {
        GlobalScope.launch {
            catFactsFacade.getCatFacts()
        }
    }

    fun onCatFactChosen(catFact: CatFact) {
        chosenCatFact.postValue(catFact)
    }

    fun catFactsObservable(): LiveData<Result<List<CatFact>>> = catFactsFacade.catFactsObservable()

    fun chosenCatFactObservable(): LiveData<CatFact> = chosenCatFact
}